package com.task.weaver.domain.authorization.service;

import com.task.weaver.domain.authorization.dto.OAuthProfile;
import com.task.weaver.domain.authorization.dto.OAuthToken;

public interface OAuthCallbackService {

    OAuthProfile getProfile (String accessToken);

    OAuthToken getAccessToken (String authorizationCode);

}
