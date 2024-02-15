package com.task.weaver.domain.authorization.service;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.TokenResponse;

public interface AuthorizationService {
	TokenResponse login (RequestSignIn requestSignIn);
}
