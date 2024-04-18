package com.task.weaver.domain.authorization.service;

import com.task.weaver.common.aop.annotation.LoggingStopWatch;
import com.task.weaver.domain.authorization.dto.MemberProjectDTO;
import com.task.weaver.domain.authorization.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.dto.response.ResponseUserOauth;
import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.domain.member.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.member.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.member.user.dto.response.ResponseUuid;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.http.HttpHeaders;

public interface MemberService {

	ResponseToken reissue (String refreshToken, String loginType);

	void logout(String refreshToken);

	Boolean checkMail(String email);

	Boolean checkId(String id);

	Boolean checkNickname(String nickname);

	@LoggingStopWatch
	ResponseUuid getUuid(String email, Boolean checked);

	@LoggingStopWatch
	ResponseGetMember getMember(UUID userId);

	@LoggingStopWatch
	ResponseUserIdNickname getMember(String email, Boolean checked);

	@LoggingStopWatch
	ResponseGetUserForFront getMemberFromToken(HttpServletRequest request);

	@LoggingStopWatch
	ResponsePageResult<MemberProjectDTO, Object[]> getMembers(RequestGetUserPage requestGetUserPage);

	@LoggingStopWatch
	ResponseUserOauth.AllMember getMembersForTest();

	ResponseToken getAuthentication(Member member);

	static HttpHeaders setCookieAndHeader(ResponseToken responseToken) {
		return null;
	}
	static HttpHeaders setCookieAndHeader(ResponseReIssueToken reIssueToken) {
		return null;
	}

	default MemberProjectDTO entityToDTO(Member member, UserOauthMember userOauthMember) {
		return MemberProjectDTO.builder()
				.memberId(member.getId())
				.userId(userOauthMember.getUuid())
				.nickname(userOauthMember.getNickname())
				.userProfileImage(String.valueOf(userOauthMember.getProfileImage()))
				.build();
	}
}
