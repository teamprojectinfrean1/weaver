package com.task.weaver.domain.authorization.service.impl;

import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.common.redis.service.RedisService;
import com.task.weaver.common.util.CookieUtil;
import com.task.weaver.common.util.HttpHeaderUtil;
import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.common.redis.RefreshToken;
import com.task.weaver.common.redis.RefreshTokenRepository;
import com.task.weaver.domain.authorization.service.AuthorizationService;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import com.task.weaver.domain.useroauthmember.entity.UserOauthMember;
import java.util.UUID;
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
	private final RedisService redisService;
	private final RefreshTokenRepository refreshTokenRepository;

	@Override
	@Transactional(readOnly = true)
	public ResponseToken weaverLogin(RequestSignIn requestSignIn) {

		User user = userRepository.findByUserId(requestSignIn.id())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, " 해당 ID가 존재하지 않습니다."));
		if(!isCheckPassword(requestSignIn)){
			throw new InvalidPasswordException(new Throwable(requestSignIn.password()));
		}

		Authentication authentication = getAuthentication(requestSignIn.id(), requestSignIn.password());
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
		String accessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.WEAVER);
		saveRefreshToken(user.getUserId(), refreshToken);

		return ResponseToken.builder()
			.accessToken("Bearer " + accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	private Authentication getAuthentication(final String principal, final String credentials) {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(principal, credentials);
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

		log.info(authentication.getName());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	private void saveRefreshToken(final UUID uuid, final String refreshToken) {
		refreshTokenRepository.save(new RefreshToken(uuid, refreshToken));
	}

	public ResponseToken authenticationOAuthUser(UserOauthMember userOauthMember) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(userOauthMember.getId(),
				userOauthMember.getLoginType());

		SecurityContextHolder.getContext().setAuthentication(authentication);

		log.info("Authentication Object = {}", SecurityContextHolder.getContext());
		String accessToken = jwtTokenProvider.createAccessToken(authentication, userOauthMember.getLoginType());
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
		saveRefreshToken(userOauthMember.getId(), refreshToken);

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

	public ResponseToken reissue(String refreshToken, String loginType) {

		// refresh token 유효성 검증
		jwtTokenProvider.validateToken(refreshToken);

		Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

		// log.info("resolvedToken : " + resolvedToken);
		log.info("authentication.getName() : " + authentication.getName());

		// authentication.getname()으로 redis에 있는 refresh token 가져오기
		RefreshToken currentRefreshToken = refreshTokenRepository.findByRefreshToken(refreshToken)
			.orElseThrow(() -> new RuntimeException(""));

		// refresh token, redis 에 저장된 것과 일치하는지 검증
		log.info("resolvedToken : " + refreshToken);
		log.info("findrefreshtoken : " + currentRefreshToken.getRefreshToken());
		if(!refreshToken.equals(currentRefreshToken.getRefreshToken()))
			throw new IllegalArgumentException(""); // 예외 처리 추후 수정

		// accessToken과 refreshToken 모두 재발행
		String newRefreshToken = jwtTokenProvider.createRefreshToken(authentication);
		String newAccessToken = jwtTokenProvider.createAccessToken(authentication, LoginType.fromName(loginType));

		// redis 에 새로 발급한 refreshtoken 저장
		refreshTokenRepository.save(new RefreshToken(currentRefreshToken.getId(), newRefreshToken));

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

		refreshTokenRepository.deleteById(authentication.getName());
	}

	@Override
	public Boolean checkMail(String email) {
		return userRepository.findByEmail(email).isEmpty();
	}

	@Override
	public Boolean checkId(String id) {
		return userRepository.findByUserId(id).isEmpty();
	}

	@Override
	public Boolean checkNickname(String nickname) {
		return userRepository.findByNickname(nickname).isEmpty();
	}

	public static HttpHeaders setCookieAndHeader(final ResponseToken responseToken) {
		HttpHeaders headers = new HttpHeaders();
		HttpHeaderUtil.setAccessToken(headers, responseToken.accessToken());
		CookieUtil.setRefreshCookie(headers, responseToken.refreshToken());
		return headers;
	}

	public static HttpHeaders setCookieAndHeader(final ResponseReIssueToken reIssueToken) {
		HttpHeaders headers = new HttpHeaders();
		HttpHeaderUtil.setAccessToken(headers, reIssueToken.accessToken());
		CookieUtil.setRefreshCookie(headers, reIssueToken.refreshToken());
		return headers;
	}
}
