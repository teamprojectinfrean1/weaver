package com.task.weaver.domain.authorization.service;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.ResponseReIssueToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import org.springframework.http.HttpHeaders;

public interface AuthorizationService {
	ResponseToken weaverLogin(RequestSignIn requestSignIn);
	ResponseToken reissue (String refreshToken);
	void logout(String refreshToken);
	Boolean checkMail(String email);
	Boolean checkId(String id);
	Boolean checkNickname(String nickname);

	static HttpHeaders setCookieAndHeader(ResponseToken responseToken) {
		return null;
	}

	static HttpHeaders setCookieAndHeader(ResponseReIssueToken reIssueToken) {
		return null;
	}
}
