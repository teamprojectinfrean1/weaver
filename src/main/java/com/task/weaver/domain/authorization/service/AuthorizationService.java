package com.task.weaver.domain.authorization.service;

import com.task.weaver.domain.authorization.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.dto.response.ResponseUserOauth;
import com.task.weaver.domain.authorization.entity.UserOauthMember;
import com.task.weaver.domain.member.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.member.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.member.user.dto.response.ResponseUuid;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import org.springframework.http.HttpHeaders;

public interface AuthorizationService {
	ResponseToken reissue (String refreshToken, String loginType);
	void logout(String refreshToken);
	Boolean checkMail(String email);
	Boolean checkId(String id);
	Boolean checkNickname(String nickname);

	ResponseUuid getUuid(String email, Boolean checked);
	ResponseGetMember getMember(UUID userId);
	ResponseUserIdNickname getMember(String email, Boolean checked);
	ResponseGetUserForFront getMemberFromToken(HttpServletRequest request);
	ResponsePageResult<ResponseGetUserList, User> getMembers(RequestGetUserPage requestGetUserPage);
	ResponseUserOauth.AllMember getMembersForTest();

	ResponseToken getAuthentication(UserOauthMember userOauthMember);
	static HttpHeaders setCookieAndHeader(ResponseToken responseToken) {
		return null;
	}
	static HttpHeaders setCookieAndHeader(ResponseReIssueToken reIssueToken) {
		return null;
	}
}
