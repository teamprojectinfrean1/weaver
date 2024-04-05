package com.task.weaver.domain.authorization.service;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.oauth.entity.OauthMember;

public interface AuthorizationService {
	ResponseToken weaverLogin(RequestSignIn requestSignIn);
	ResponseToken oauthLogin(OauthMember oauthMember);
	ResponseToken reissue (String refreshToken);
	void logout(String refreshToken);
	Boolean checkMail(String email);
	Boolean checkId(String id);
	Boolean checkNickname(String nickname);
}
