package com.task.weaver.domain.authorization.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
	public ResponseEntity<?> login(@RequestBody RequestSignIn requestSignIn) {
		// access token -> header? body? cookie?

		TokenResponse tokenResponse = authorizationService.login(requestSignIn);
		// access token만 return
		return ResponseEntity.ok().body(tokenResponse.accessToken());
	}

	// @GetMapping("/reissue")
	// public ResponseEntity<?> reissue(@Request)
}
