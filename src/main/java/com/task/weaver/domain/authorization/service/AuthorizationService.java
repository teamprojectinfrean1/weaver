package com.task.weaver.domain.authorization.service;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.util.JWToken;

public interface AuthorizationService {
	JWToken login (RequestSignIn requestSignIn);
}
