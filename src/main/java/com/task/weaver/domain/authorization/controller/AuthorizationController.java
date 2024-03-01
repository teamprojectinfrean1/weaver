package com.task.weaver.domain.authorization.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.request.RequestToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.service.AuthorizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Tag(name = "Authorization Controller", description = "인증 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthorizationController {
	private final AuthorizationService authorizationService;

	@Operation(summary = "로그인", description = "로그인")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestSignIn requestSignIn, HttpServletResponse res) {
		// access token -> header? body? cookie?
		ResponseToken responseToken = authorizationService.login(requestSignIn);

		log.info("refreshToken : " + responseToken.refreshToken());

		// refresh token cookie에 담기
		ResponseCookie cookie = ResponseCookie.from("refresh-token", responseToken.refreshToken())
			.maxAge(60 * 60 * 24 * 15)
			.httpOnly(true)
			.secure(true)
			.domain("")
			.path("/")
			.sameSite("None")
			.build();

		res.setHeader("Set-Cookie", cookie.toString());
		log.info("login - cookie : " + cookie);

		// access token body에 담아 return
		return ResponseEntity.ok().body(responseToken.accessToken());
	}

	@Operation(summary = "reissue", description = "refresh token 재발급")
	@GetMapping("/reissue")
	public ResponseEntity<?> reissue(@RequestHeader("Refresh-Token") String accessToken, @CookieValue(value = "refresh-token", required = false) Cookie cookie, HttpServletResponse res) {
		log.info("reissue controller - cookie : " + cookie.getValue());

		ResponseToken responseToken = authorizationService.reissue(
			RequestToken.builder()
				.accessToken(accessToken)
				.refreshToken(cookie.getValue())
				.build());

		// refresh token cookie에 담기
		ResponseCookie newCookie = ResponseCookie.from("refresh-token", responseToken.refreshToken())
			.maxAge(60 * 60 * 24 * 15)
			.httpOnly(true)
			.secure(true)
			.domain("")
			.path("/")
			.sameSite("None")
			.build();

		res.setHeader("Set-Cookie", newCookie.toString());

		// access token body에 담아 return
		return ResponseEntity.ok().body(responseToken.accessToken());
	}

	@Operation(summary = "로그아웃", description = "로그아웃")
	@GetMapping("/logout")
	public ResponseEntity<?> logout(@CookieValue(value = "refresh-token", required = false) Cookie cookie, HttpServletResponse res) {
		authorizationService.logout(cookie.getValue());
		cookie.setMaxAge(0);
		res.setHeader("Set-Cookie", cookie.toString());
		return ResponseEntity.ok().body("-- logout --");
	}
}
