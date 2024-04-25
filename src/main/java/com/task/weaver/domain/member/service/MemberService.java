package com.task.weaver.domain.member.service;

import com.task.weaver.domain.member.dto.MemberProjectDTO;
import com.task.weaver.domain.member.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.member.dto.response.ResponseUserOauth;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUuid;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;

public interface MemberService {

	ResponseToken reissue (String refreshToken, String loginType);

	void logout(String refreshToken);

	Boolean checkMail(String email);

	Boolean checkId(String id);

	Boolean checkNickname(String nickname);

	ResponseUuid getUuid(String email, Boolean checked);

	ResponseGetMember getMember(UUID userId);

	ResponseUserIdNickname getMember(String email, Boolean checked);

	ResponseGetUserForFront getMemberFromToken(HttpServletRequest request);

	Page<MemberProjectDTO> getMembers(int page, int size, UUID projectId);

	ResponseUserOauth.AllMember getMembersForTest();

	ResponseToken getAuthentication(Member member);

	static HttpHeaders setCookieAndHeader(ResponseToken responseToken) {
		return null;
	}
	static HttpHeaders setCookieAndHeader(ResponseReIssueToken reIssueToken) {
		return null;
	}

	default MemberProjectDTO entityToDTO(UserOauthMember userOauthMember) {
		return MemberProjectDTO.builder()
				.memberId(userOauthMember.getMemberUuid())
				.userId(userOauthMember.getUuid())
				.nickname(userOauthMember.getNickname())
				.userProfileImage(String.valueOf(userOauthMember.getProfileImage()))
				.hasAssigneeIssueInProgress(false)
				.build();
	}
}
