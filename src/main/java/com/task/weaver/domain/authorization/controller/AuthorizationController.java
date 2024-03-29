package com.task.weaver.domain.authorization.controller;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.authorization.dto.request.EmailCheckDto;
import com.task.weaver.domain.authorization.dto.request.EmailRequest;
import com.task.weaver.domain.authorization.dto.response.EmailCode;
import com.task.weaver.domain.authorization.service.impl.MailSendService;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
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
	private final MailSendService mailService;
	private final UserService userService;

	@Operation(summary = "회원가입", description = "사용자가 회원가입")
	@PostMapping("/join")
	public ResponseEntity<ResponseGetUser> addUser(@RequestBody RequestCreateUser requestCreateUser){
		log.info("controller - join - before");
		ResponseGetUser responseGetUser = userService.addUser(requestCreateUser);
		log.info("controller - join - after");
		return ResponseEntity.status(HttpStatus.OK).body(responseGetUser);
	}

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
			// .secure(true)
			.domain("")
			.path("/")
			// .sameSite("None")
			.build();

		res.setHeader("Set-Cookie", cookie.toString());
		log.info("login - cookie : " + cookie);

		// access token body에 담아 return
		return ResponseEntity.ok().body(responseToken.accessToken());
	}

	@Operation(summary = "reissue", description = "refresh token 재발급")
	@GetMapping("/reissue")
	public ResponseEntity<?> reissue(@CookieValue(value = "refresh-token", required = false) Cookie cookie, HttpServletResponse res) {
		log.info("reissue controller - cookie : " + cookie.getValue());

		ResponseToken responseToken = authorizationService.reissue(cookie.getValue());
    
		// refresh token cookie에 담기
		ResponseCookie newCookie = ResponseCookie.from("refresh-token", responseToken.refreshToken())
			.maxAge(60 * 60 * 24 * 15)
			.httpOnly(true)
			// .secure(true)
			.domain("")
			.path("/")
			// .sameSite("None")
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

	@Operation(summary = "이메일 중복 체크", description = "이메일 중복을 체크")
	@GetMapping("/checkMail")
	@Parameter(name = "email", description = "이메일 입력", in = ParameterIn.QUERY)
	public ResponseEntity<DataResponse<Boolean>> checkMail(@RequestParam("email") String email){
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "중복 체크 동작", authorizationService.checkMail(email)), HttpStatus.OK);
	}

	@Operation(summary = "아이디 중복 체크", description = "아이디 중복을 체크")
	@GetMapping("/checkId")
	@Parameter(name = "id", description = "아이디 입력", in = ParameterIn.QUERY)
	public ResponseEntity<DataResponse<Boolean>> checkId(@RequestParam("id") String id){
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "중복 체크 동작", authorizationService.checkId(id)), HttpStatus.OK);
	}

	@Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크")
	@GetMapping("/checkNickname")
	@Parameter(name = "nickname", description = "닉네임 입력", in = ParameterIn.QUERY)
	public ResponseEntity<DataResponse<Boolean>> checkNickname(@RequestParam("nickname") String nickname){
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "중복 체크 동작", authorizationService.checkNickname(nickname)), HttpStatus.OK);
	}

	@PostMapping("/findId/verification/request")
	public ResponseEntity<DataResponse<EmailCode>> mailSendForId(@RequestBody @Valid EmailRequest emailDto) {
		log.info("Find ID, verify email: {}", emailDto.email());
		return ResponseEntity.ok(
				DataResponse.of(HttpStatus.OK, "인증 코드 전송 성공", mailService.sendVerificationEmail(emailDto.email())));
	}

	@PostMapping("/findId/verification/check")
	public ResponseEntity<DataResponse<ResponseUserIdNickname>> AuthCheckForId(
			@RequestBody @Valid EmailCheckDto emailCheckDto) {
		Boolean checked = mailService.CheckAuthNum(emailCheckDto.email(), emailCheckDto.verificationCode());
		ResponseUserIdNickname targetUser = userService.getUser(emailCheckDto.email(), checked);
		return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "해당 유저를 반환합니다.", targetUser));
	}

	@PostMapping("/findPassword/verification/request")
	public ResponseEntity<DataResponse<EmailCode>> mailSendForPassword(@RequestBody @Valid EmailRequest emailDto) {
		log.info("비밀번호 찾기 이메일 인증 :" + emailDto.email());
		return ResponseEntity.ok(
				DataResponse.of(HttpStatus.OK, "인증 코드 전송 성공", mailService.joinEmail(emailDto.email())));
	}

	@PostMapping("/findPassword/verification/check")
	public ResponseEntity<DataResponse<ResponseUserIdNickname>> AuthCheckForPassword(@RequestBody @Valid EmailCheckDto emailCheckDto) {
		Boolean checked = mailService.CheckAuthNum(emailCheckDto.email(), emailCheckDto.verificationCode());
		ResponseUserIdNickname targetUser = userService.getUser(emailCheckDto.email(), checked);
		return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "verificationCode 인증 성공", targetUser));
	}

	@PutMapping("/findPassword/verification/update")
	public ResponseEntity<MessageResponse> AuthPasswordUpdate(@RequestBody @Valid RequestUpdatePassword requestUpdatePassword) {
		userService.updateUser(requestUpdatePassword);
		return ResponseEntity.ok(MessageResponse.of(HttpStatus.OK, "비밀번호 변경 성공"));
	}
}
