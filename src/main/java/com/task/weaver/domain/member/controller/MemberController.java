package com.task.weaver.domain.member.controller;

import static com.task.weaver.domain.member.service.impl.MemberServiceImpl.setCookieAndHeader;

import com.task.weaver.common.aop.annotation.Logger;
import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.member.dto.MemberProjectDTO;
import com.task.weaver.domain.member.dto.response.GetMemberListResponse;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.member.dto.response.ResponseUserOauth.AllMember;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.service.MemberService;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetUserForFront;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Member Controller", description = "멤버 공통 기능 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

	private final MemberService memberService;

	@Logger
	@Operation(summary = "reissue", description = "refresh token 재발급")
	@Parameter(name = "refreshToken", description = "리프래쉬 토큰", in = ParameterIn.COOKIE)
	@GetMapping(value = "/reissue")
	public ResponseEntity<?> reissue(@CookieValue(value = "refreshToken") String refreshToken,
									 @RequestParam String loginType) {
		log.info("reissue controller - refreshToken : " + refreshToken);
		log.error("loginType = {}", loginType);
		ResponseToken responseToken = memberService.reissue(refreshToken, loginType);
		HttpHeaders headers = setCookieAndHeader(responseToken);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.CREATED, "Token 재발급 성공", headers, true),
				HttpStatus.CREATED);
	}

	@Logger
	@Operation(summary = "로그아웃", description = "로그아웃")
	@GetMapping("/logout")
	public ResponseEntity<?> logout(@CookieValue(value = "refresh-token") Cookie cookie, HttpServletResponse res) {
		memberService.logout(cookie.getValue());
		cookie.setMaxAge(0);
		res.setHeader("Set-Cookie", cookie.toString());
		return ResponseEntity.ok().body("-- logout --");
	}

	@Logger
	@Operation(summary = "사용자 한 명 조회", description = "사용자 한명을 조회")
	@GetMapping("/{uuid}")
	public ResponseEntity<DataResponse<ResponseGetMember>> getMember(@PathVariable("uuid") UUID uuid) {
		ResponseGetMember responseGetMember = memberService.getMember(uuid);
		return ResponseEntity.ok().body(DataResponse.of(HttpStatus.OK, "UUID로 회원 조회 성공", responseGetMember, true));
	}

	@Logger
	@Operation(summary = "프로젝트 구성원 조회", description = "프로젝트에 소속된 인원 페이지 조회")
	@GetMapping("/project/user/page")
	public ResponseEntity<DataResponse<ResponsePageResult<GetMemberListResponse, Member>>> getMembersFromProject(
			@RequestParam int page,
			@RequestParam int size,
			@RequestParam UUID projectId) {

		ResponsePageResult<GetMemberListResponse, Member> responseGetUserLists = memberService.getMemberList(page, size, projectId);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 구성원 조회 성공", responseGetUserLists, true),
				HttpStatus.OK);
	}

	@Logger
	@Operation(summary = "프로젝트 구성원 조회", description = "프로젝트에 소속된 인원 전체 조회")
	@GetMapping("/project/user/list")
	public ResponseEntity<DataResponse<List<MemberProjectDTO>>> getMembersFromProject(@RequestParam UUID projectId) {
		List<MemberProjectDTO> responseGetUserLists = memberService.getMembers(projectId);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "프로젝트 구성원 조회 성공", responseGetUserLists, true),
				HttpStatus.OK);
	}

	@Logger
	@Operation(summary = "개발자용 유저 리스트 확인 api", description = "생성된 유저 전부 조회")
	@GetMapping("/list/test")
	public ResponseEntity<DataResponse<AllMember>> getMemberForTest() {
		AllMember responseGetUsers = memberService.getMembersForTest();
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "유저 리스트 전부 조회", responseGetUsers, true),
				HttpStatus.OK);
	}

	@Logger
	@Operation(summary = "토큰 기반 유저 조회", description = "로그인 직후, 토큰 기반으로 유저 정보 조회")
	@Parameter(name = "Authorization", description = "토큰", in = ParameterIn.HEADER)
	@GetMapping("/token")
	public ResponseEntity<DataResponse<ResponseGetUserForFront>> getMemberFromToken(HttpServletRequest request) {
		ResponseGetUserForFront responseGetUser = memberService.getMemberFromToken(request);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "토큰 기반 유저 정보 반환 성공", responseGetUser, true),
				HttpStatus.OK);
	}
}
