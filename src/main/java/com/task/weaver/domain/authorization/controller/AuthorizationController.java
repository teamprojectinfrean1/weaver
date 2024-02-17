package com.task.weaver.domain.authorization.controller;

import org.apache.coyote.Response;
import org.springframework.http.ResponseCookie;
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
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {
	private final AuthorizationService authorizationService;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestSignIn requestSignIn, HttpServletResponse res) {
		// access token -> header? body? cookie?
		TokenResponse tokenResponse = authorizationService.login(requestSignIn);

		log.info("refreshToken : " + tokenResponse.refreshToken());

		// refresh token cookie에 담기
		ResponseCookie cookie = ResponseCookie.from("refresh-token", tokenResponse.refreshToken())
			.maxAge(60 * 60 * 24 * 15)
			.httpOnly(true)
			.secure(true)
			.domain("")
			.path("/")
			.sameSite("None")
			.build();

		res.setHeader("Set-Cookie", cookie.toString());

		// access token body에 담아 return
		return ResponseEntity.ok().body(tokenResponse.accessToken());
	}

	// @GetMapping("/reissue")
	// public ResponseEntity<?> reissue(@Request)
}
