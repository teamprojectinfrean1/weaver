package com.task.weaver.domain.authorization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.TokenResponse;
import com.task.weaver.domain.authorization.service.AuthorizationService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {
	private final AuthorizationService authorizationService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestSignIn requestSignIn, HttpServletResponse httpServletResponse) {

		TokenResponse tokenResponse = authorizationService.login(requestSignIn);
		return ResponseEntity.ok().body(tokenResponse);
	}
}
