package com.task.weaver.domain.authorization.controller;

import static com.task.weaver.domain.authorization.service.impl.AuthorizationServiceImpl.setCookieAndHeader;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.authorization.dto.request.EmailCheckDto;
import com.task.weaver.domain.authorization.dto.request.EmailRequest;
import com.task.weaver.domain.authorization.dto.response.EmailCode;
import com.task.weaver.domain.authorization.service.impl.MailSendService;
import com.task.weaver.domain.member.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.member.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.member.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.member.user.dto.response.ResponseUuid;
import com.task.weaver.domain.member.user.service.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
	public ResponseEntity<DataResponse<ResponseGetUser>> addUser(RequestCreateUser requestCreateUser,
												   MultipartHttpServletRequest multipartHttpServletRequest) throws IOException {

		MultipartFile profileImage = multipartHttpServletRequest.getFile("profileImage");

		log.info("controller - join - before");
		ResponseGetUser responseGetUser = userService.addUser(requestCreateUser, profileImage);
		log.info("controller - join - after");
		return ResponseEntity.status(HttpStatus.OK).body(DataResponse.of(HttpStatus.OK, "회원 가입 성공", responseGetUser, true));
	}

	@Operation(summary = "로그인", description = "로그인")
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody RequestSignIn requestSignIn) {
		ResponseToken responseToken = authorizationService.weaverLogin(requestSignIn);
		log.info("refreshToken : " + responseToken.refreshToken());
		HttpHeaders headers = setCookieAndHeader(responseToken);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.CREATED, "Weaver login successfully", headers, true),
				HttpStatus.CREATED);
	}

	@Operation(summary = "reissue", description = "refresh token 재발급")
	@GetMapping("/reissue")
	public ResponseEntity<?> reissue(@CookieValue(value = "refreshToken") String refreshToken) {
		log.info("reissue controller - refreshToken : " + refreshToken);
		ResponseToken responseToken = authorizationService.reissue(refreshToken);
		HttpHeaders headers = setCookieAndHeader(responseToken);
		return new ResponseEntity<>(MessageResponse.of(HttpStatus.CREATED, "Token 재발급 성공", true), headers, HttpStatus.CREATED);
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
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "중복 체크 동작", authorizationService.checkMail(email), true), HttpStatus.OK);
	}

	@Operation(summary = "아이디 중복 체크", description = "아이디 중복을 체크")
	@GetMapping("/checkId")
	@Parameter(name = "id", description = "아이디 입력", in = ParameterIn.QUERY)
	public ResponseEntity<DataResponse<Boolean>> checkId(@RequestParam("id") String id){
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "중복 체크 동작", authorizationService.checkId(id), true), HttpStatus.OK);
	}

	@Operation(summary = "닉네임 중복 체크", description = "닉네임 중복을 체크")
	@GetMapping("/checkNickname")
	@Parameter(name = "nickname", description = "닉네임 입력", in = ParameterIn.QUERY)
	public ResponseEntity<DataResponse<Boolean>> checkNickname(@RequestParam("nickname") String nickname){
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "중복 체크 동작", authorizationService.checkNickname(nickname), true), HttpStatus.OK);
	}

	@Operation(summary = "이메일 전송", description = "랜덤 번호를 담은 이메일 전송")
	@Parameter(name = "EmailRequest", description = "사용자 이메일", in = ParameterIn.QUERY)
	@PostMapping("/findId/verification/request")
	public ResponseEntity<DataResponse<EmailCode>> mailSendForId(@RequestBody @Valid EmailRequest emailDto) {
		log.info("Find ID, verify email: {}", emailDto.email());
		return ResponseEntity.ok(
				DataResponse.of(HttpStatus.OK, "인증 코드 전송 성공", mailService.joinEmail(emailDto.email()), true));
	}

	@Operation(summary = "인증 번호 확인", description = "서버에 저장된 랜덤 번호와 사용자 입력 번호 검증")
	@Parameter(name = "EmailCheckDto", description = "사용자 이메일, 인증 번호", in = ParameterIn.QUERY)
	@PostMapping("/findId/verification/check")
	public ResponseEntity<DataResponse<ResponseUserIdNickname>> AuthCheckForId(
			@RequestBody @Valid EmailCheckDto emailCheckDto) {
		Boolean checked = mailService.CheckAuthNum(emailCheckDto.email(), emailCheckDto.verificationCode());
		ResponseUserIdNickname targetUser = userService.getUser(emailCheckDto.email(), checked);
		return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "해당 유저를 반환합니다.", targetUser, true));
	}

	@Operation(summary = "이메일 전송", description = "랜덤 번호를 담은 이메일 전송")
	@Parameter(name = "EmailRequest", description = "사용자 이메일", in = ParameterIn.QUERY)
	@PostMapping("/findPassword/verification/request")
	public ResponseEntity<DataResponse<EmailCode>> mailSendForPassword(@RequestBody @Valid EmailRequest emailDto) {
		log.info("비밀번호 찾기 이메일 인증 :" + emailDto.email());
		return ResponseEntity.ok(
				DataResponse.of(HttpStatus.OK, "인증 코드 전송 성공", mailService.joinEmail(emailDto.email()), true));
	}

	@Operation(summary = "인증 번호 확인", description = "서버에 저장된 랜덤 번호와 사용자 입력 번호 검증")
	@Parameter(name = "EmailCheckDto", description = "사용자 이메일, 인증 번호", in = ParameterIn.QUERY)
	@PostMapping("/findPassword/verification/check")
	public ResponseEntity<DataResponse<ResponseUuid>> AuthCheckForPassword(@RequestBody @Valid EmailCheckDto emailCheckDto) {
		Boolean checked = mailService.CheckAuthNum(emailCheckDto.email(), emailCheckDto.verificationCode());
		ResponseUuid targetUser = userService.getUuid(emailCheckDto.email(), checked);
		return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "verificationCode 인증 성공", targetUser, true));
	}

	@Operation(summary = "비밀번호 재설정", description = "인증 확인된 사용자 비밀번호 재설정")
	@Parameter(name = "RequestUpdatePassword", description = "UUID, 재설정 비밀번호", in = ParameterIn.QUERY)
	@PutMapping("/findPassword/verification/update")
	public ResponseEntity<MessageResponse> AuthPasswordUpdate(@RequestBody @Valid RequestUpdatePassword requestUpdatePassword) {
		userService.updateUser(requestUpdatePassword);
		return ResponseEntity.ok(MessageResponse.of(HttpStatus.OK, "비밀번호 변경 성공", true));
	}
}
