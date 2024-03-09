package com.task.weaver.domain.authorization.service;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.request.RequestToken;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;

public interface AuthorizationService {
	ResponseToken login (RequestSignIn requestSignIn);
	ResponseToken reissue (RequestToken requestToken);
	void logout(String refreshToken);
	Boolean checkMail(String email);
	Boolean checkId(String id);
	Boolean checkNickname(String nickname);
}
