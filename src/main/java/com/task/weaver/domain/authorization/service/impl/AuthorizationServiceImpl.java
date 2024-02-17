package com.task.weaver.domain.authorization.service.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.TokenResponse;
import com.task.weaver.domain.authorization.redis.RefreshToken;
import com.task.weaver.domain.authorization.redis.RefreshTokenRedisRepository;
import com.task.weaver.domain.authorization.service.AuthorizationService;
import com.task.weaver.domain.authorization.util.JwtTokenProvider;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtTokenProvider jwtTokenProvider;

	private final UserService userService;
	private final RefreshTokenRedisRepository refreshTokenRedisRepository;

	@Override
	@Transactional(readOnly = true)
	public TokenResponse login(RequestSignIn requestSignIn) {
		// userId check
		ResponseUser user = userService.getUser(requestSignIn.email());

		// password check
		if(!isCheckPassword(requestSignIn)){
			throw new InvalidPasswordException(new Throwable(requestSignIn.password()));
		}

		// 아직 인증되지 않은 객체로 추후 모든 인증이 완료되면 인증된 생성자로 authentication 객체가 생성된다.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(requestSignIn.email(), requestSignIn.password());
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(usernamePasswordAuthenticationToken);

		// 실패하면 securitycontextholder를 비우고, 성공하면 securitycontextholder에 authentication을 세팅
		SecurityContextHolder.getContext().setAuthentication(authentication);

		// refresh token 발급
		String refreshToken = jwtTokenProvider.createRefreshToken(authentication);
		// access token 발급
		String accessToken = jwtTokenProvider.createAccessToken(authentication);

		// refresh token redis에 저장
		refreshTokenRedisRepository.save(new RefreshToken(String.valueOf(user.getUser_id()), refreshToken, accessToken));

		// 기존 토큰 있으면 수정, 없으면 생성

		// accessToken, refreshToken 리턴
		return TokenResponse.builder()
			.accessToken("Bearer-" + accessToken)
			.refreshToken("Bearer-" + refreshToken)
			.build();
	}

	private boolean isCheckPassword(RequestSignIn requestSignIn) {
		ResponseUser user = userService.getUser(requestSignIn.email());
		log.info(user.getPassword());
		log.info(requestSignIn.password());
		log.info(passwordEncoder.matches(requestSignIn.password(), user.getPassword()) ? "true" : "false");
		return passwordEncoder.matches(requestSignIn.password(), user.getPassword()); // 암호화된 비밀번호가 뒤로 와야 한다 순서
	}
}
