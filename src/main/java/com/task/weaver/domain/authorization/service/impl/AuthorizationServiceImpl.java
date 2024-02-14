package com.task.weaver.domain.authorization.service.impl;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.service.AuthorizationService;
import com.task.weaver.domain.authorization.util.JWToken;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorizationServiceImpl implements AuthorizationService {

	private PasswordEncoder passwordEncoder;
	private AuthenticationManagerBuilder authenticationManagerBuilder;

	private final UserService userService;

	@Override
	public JWToken login(RequestSignIn requestSignIn) {

		if(!isCheckPassword(requestSignIn)){
			throw new InvalidPasswordException(new Throwable(requestSignIn.password()));
		}

		// 아직 인증되지 않은 객체로 추후 모든 인증이 완료되면 인증된 생성자로 authentication 객체가 생성된다.
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(requestSignIn.email(), requestSignIn.password());
		Authentication authentication = authenticationManagerBuilder.getObject().authenticate(token);
		// 실패하면 securitycontextholder를 비우고, 성공하면 securitycontextholder에 authentication을 세팅
		SecurityContextHolder.getContext().setAuthentication(authentication);

		return null;
	}

	private boolean isCheckPassword(RequestSignIn requestSignIn) {
		ResponseUser user = userService.getUser(requestSignIn.email());
		return passwordEncoder.matches(requestSignIn.password(), user.getPassword());
	}
}
