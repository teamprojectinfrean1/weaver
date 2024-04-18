package com.task.weaver.domain.authorization.controller;

import static com.task.weaver.domain.authorization.service.impl.MemberServiceImpl.setCookieAndHeader;

import com.task.weaver.common.response.DataResponse;
import com.task.weaver.common.response.MessageResponse;
import com.task.weaver.domain.authorization.dto.MemberProjectDTO;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.dto.response.ResponseUserOauth.AllMember;
import com.task.weaver.domain.authorization.service.MemberService;
import com.task.weaver.domain.member.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.member.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authorization Controller", description = "인증 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

	private final MemberService memberService;

	@Operation(summary = "reissue", description = "refresh token 재발급")
	@GetMapping("/reissue")
	public ResponseEntity<?> reissue(@CookieValue(value = "refreshToken") String refreshToken,
									 @RequestParam String loginType) {
		log.info("reissue controller - refreshToken : " + refreshToken);

		ResponseToken responseToken = memberService.reissue(refreshToken, loginType);
		HttpHeaders headers = setCookieAndHeader(responseToken);
		return new ResponseEntity<>(MessageResponse.of(HttpStatus.CREATED, "Token 재발급 성공", true), headers, HttpStatus.CREATED);
	}

	@Operation(summary = "로그아웃", description = "로그아웃")
	@GetMapping("/logout")
	public ResponseEntity<?> logout(@CookieValue(value = "refresh-token", required = false) Cookie cookie, HttpServletResponse res) {
		memberService.logout(cookie.getValue());
		cookie.setMaxAge(0);
		res.setHeader("Set-Cookie", cookie.toString());
		return ResponseEntity.ok().body("-- logout --");
	}

	@Operation(summary = "사용자 한 명 조회", description = "사용자 한명을 조회")
	@Parameter(name = "UUID", description = "사용자 UUID", in = ParameterIn.QUERY)
	@GetMapping("/{uuid}")
	public ResponseEntity<DataResponse<ResponseGetMember>> getMember(@PathVariable("uuid") UUID uuid) {
		ResponseGetMember responseGetMember = memberService.getMember(uuid);
		return ResponseEntity.ok().body(DataResponse.of(HttpStatus.OK, "UUID로 회원 조회 성공", responseGetMember, true));
	}

	@Operation(summary = "프로젝트 구성원 조회", description = "프로젝트에 소속된 인원들 조회")
	@Parameter(name = "projectId", description = "프로젝트 id", in = ParameterIn.PATH)
	@GetMapping("/project/user-list")
	public ResponseEntity<DataResponse<ResponsePageResult<MemberProjectDTO, Object[]>>> getMembersFromProject(
			@RequestBody RequestGetUserPage requestGetUserPage) {
		ResponsePageResult<MemberProjectDTO, Object[]> responseGetUserLists = memberService.getMembers(
				requestGetUserPage);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 구성원 조회 성공", responseGetUserLists, true),
				HttpStatus.OK);
	}

	@Operation(summary = "개발자용 유저 리스트 확인 api", description = "생성된 유저 전부 조회")
	@GetMapping("/list/test")
	public ResponseEntity<DataResponse<AllMember>> getMemberForTest() {
		AllMember responseGetUsers = memberService.getMembersForTest();
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "유저 리스트 전부 조회", responseGetUsers, true),
				HttpStatus.OK);
	}

	@Operation(summary = "토큰 기반 유저 조회", description = "로그인 직후, 토큰 기반으로 유저 정보 조회")
	@Parameter(name = "Authorization", description = "토큰", in = ParameterIn.HEADER)
	@GetMapping("/token")
	public ResponseEntity<DataResponse<ResponseGetUserForFront>> getMemberFromToken(HttpServletRequest request) {
		ResponseGetUserForFront responseGetUser = memberService.getMemberFromToken(request);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "토큰 기반 유저 정보 반환 성공", responseGetUser, true),
				HttpStatus.OK);
	}
}
