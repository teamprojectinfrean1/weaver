package com.task.weaver.domain.authorization.service.impl;

import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.common.util.CookieUtil;
import com.task.weaver.common.util.HttpHeaderUtil;
import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRedisRepository;
import com.task.weaver.domain.authorization.service.AuthorizationService;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;
	private final UserRepository userRepository;
	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Override
	@Transactional(readOnly = true)
	public ResponseToken weaverLogin(RequestSignIn requestSignIn) {

		// userId check
		User user = userRepository.findByUserId(requestSignIn.id())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, " 해당 ID가 존재하지 않습니다."));

		// password check
		if(!isCheckPassword(requestSignIn)){
			throw new InvalidPasswordException(new Throwable(requestSignIn.password()));
		}

		Authentication authentication = getAuthentication(requestSignIn.id(), requestSignIn.password());

		// 실패하면 securitycontextholder를 비우고, 성공하면 securitycontextholder에 authentication을 세팅
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// refresh token 발급
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
		// access token 발급
		String accessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.WEAVER);
		// refresh token redis에 저장
		saveRefreshToken(authentication.getName(), refreshToken);

		// 기존 토큰 있으면 수정, 없으면 생성
		// accessToken, refreshToken 리턴
		return ResponseToken.builder()
			.accessToken("Bearer " + accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	private Authentication getAuthentication(final String principal, final String credentials) {
		// 아직 인증되지 않은 객체로 추후 모든 인증이 완료되면 인증된 생성자로 authentication 객체가 생성된다.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(principal, credentials);
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

		log.info(authentication.getName());
		// 실패하면 securitycontextholder를 비우고, 성공하면 securitycontextholder에 authentication을 세팅
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	private void saveRefreshToken(final String username, final String refreshToken) {
		refreshTokenRedisRepository.save(new RefreshToken(username, refreshToken));
	}

	public ResponseToken authenticationOAuthUser(OauthMember oauthMember) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(oauthMember.oauthId().oauthServerId(),
				oauthMember.oauthId().oauthServer());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		log.info("Authentication Object = {}", SecurityContextHolder.getContext());
		String accessToken = jwtTokenProvider.createAccessToken(authentication, oauthMember.loginType());
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
		saveRefreshToken(authentication.getName(), refreshToken);

		return ResponseToken.builder()
				.accessToken("Bearer " + accessToken)
				.refreshToken(refreshToken)
				.build();
	}

	private boolean isCheckPassword(RequestSignIn requestSignIn) {
		User user = userRepository.findByUserId(requestSignIn.id())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, " 해당 id가 존재하지 않습니다."));
		log.info(user.getPassword());
		log.info(requestSignIn.password());
		log.info(passwordEncoder.matches(requestSignIn.password(), user.getPassword()) ? "true" : "false");
		return passwordEncoder.matches(requestSignIn.password(), user.getPassword()); // 암호화된 비밀번호가 뒤로 와야 한다 순서
	}

	public ResponseToken reissue(String refreshToken) {

		// refresh token 유효성 검증
		jwtTokenProvider.validateToken(refreshToken);

		Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

		// log.info("resolvedToken : " + resolvedToken);
		log.info("authentication.getName() : " + authentication.getName());

		// authentication.getname()으로 redis에 있는 refresh token 가져오기
		RefreshToken findTokenEntity = refreshTokenRedisRepository.findById(authentication.getName())
			.orElseThrow(() -> new RuntimeException("")); // 예외 처리 추후 수정

		// refresh token, redis 에 저장된 것과 일치하는지 검증
		log.info("resolvedToken : " + refreshToken);
		log.info("findrefreshtoken : " + findTokenEntity.getRefreshToken());
		if(!refreshToken.equals(findTokenEntity.getRefreshToken()))
			throw new IllegalArgumentException(""); // 예외 처리 추후 수정

		// accessToken과 refreshToken 모두 재발행
		String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
		String newAccessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.WEAVER);

		// redis 에 새로 발급한 refreshtoken 저장
		refreshTokenRedisRepository.save(new RefreshToken(authentication.getName(), newRefreshToken));

		return ResponseToken.builder()
			.accessToken("Bearer "+newAccessToken)
			.refreshToken(newRefreshToken)
			.build();
	}

	/**
	 * token 앞 "Bearer-" 제거
	 * @param accessToken
	 * @return
	 */
	private String resolveToken(String accessToken) {
		if(accessToken.startsWith("Bearer "))
			return accessToken.substring(7);
		// 예외 처리 추후 수정
		throw new RuntimeException("not valid refresh token");
	}

	@Override
	public void logout(String refreshToken) {

		Authentication authentication = jwtTokenProvider.getAuthentication(resolveToken(refreshToken));

		refreshTokenRedisRepository.deleteById(authentication.getName());
	}

	@Override
	public Boolean checkMail(String email) {
		if(userRepository.findByEmail(email).isPresent())
			return false;
		return true;
	}

	@Override
	public Boolean checkId(String id) {
		if(userRepository.findByUserId(id).isPresent())
			return false;
		return true;
	}

	@Override
	public Boolean checkNickname(String nickname) {
		if(userRepository.findByNickname(nickname).isPresent())
			return false;
		return true;
	}

	public static HttpHeaders setCookieAndHeader(final ResponseToken responseToken) {
		HttpHeaders headers = new HttpHeaders();
		CookieUtil.setRefreshCookie(headers, responseToken.accessToken());
		HttpHeaderUtil.setAccessToken(headers, responseToken.accessToken());
		return headers;
	}

	public static HttpHeaders setCookieAndHeader(final ResponseReIssueToken reIssueToken) {
		HttpHeaders headers = new HttpHeaders();
		HttpHeaderUtil.setAccessToken(headers, reIssueToken.accessToken());
		CookieUtil.setRefreshCookie(headers, reIssueToken.refreshToken());
		return headers;
	}
}
