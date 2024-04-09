package com.task.weaver.common.oauth.google.client;

import com.task.weaver.common.oauth.config.GoogleOauthConfig;
import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.common.oauth.google.dto.GoogleMemberResponse;
import com.task.weaver.common.oauth.google.dto.GoogleToken;
import com.task.weaver.domain.member.oauth.client.OauthMemberClient;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements OauthMemberClient {

    private final GoogleApiClient googleApiClient;
    private final GoogleOauthConfig googleOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.GOOGLE;
    }

    @Override
    public OauthMember fetch(final String authCode) {
        GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode));
        GoogleMemberResponse googleMemberResponse =
                googleApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return googleMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", googleOauthConfig.clientId());
        params.add("client_secret", googleOauthConfig.clientSecret());
        params.add("code", authCode);
        params.add("redirect_uri", googleOauthConfig.redirectUri());
        return params;
    }
}
