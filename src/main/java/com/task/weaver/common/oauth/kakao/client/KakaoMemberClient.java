package com.task.weaver.common.oauth.kakao.client;

import com.task.weaver.common.oauth.config.KakaoOauthConfig;
import com.task.weaver.domain.member.oauth.client.OauthMemberClient;
import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.common.oauth.kakao.dto.KakaoMemberResponse;
import com.task.weaver.common.oauth.kakao.dto.KakaoToken;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoMemberClient implements OauthMemberClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoOauthConfig kakaoOauthConfig;

    @Override
    public OauthServerType supportServer() {
        return OauthServerType.KAKAO;
    }

    @Override
    public OauthUser fetch(String authCode) {
        KakaoToken tokenInfo = kakaoApiClient.fetchToken(tokenRequestParams(authCode));
        KakaoMemberResponse kakaoMemberResponse =
                kakaoApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
        return kakaoMemberResponse.toDomain();
    }

    private MultiValueMap<String, String> tokenRequestParams(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOauthConfig.clientId());
        params.add("redirect_uri", kakaoOauthConfig.redirectUri());
        params.add("code", authCode);
        params.add("client_secret", kakaoOauthConfig.clientSecret());
        return params;
    }
}
