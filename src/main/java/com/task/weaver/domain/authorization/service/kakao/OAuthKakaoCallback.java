package com.task.weaver.domain.authorization.service.kakao;

import com.task.weaver.domain.authorization.dto.OAuthProfile;
import com.task.weaver.domain.authorization.dto.OAuthToken;
import com.task.weaver.domain.authorization.service.OAuthCallbackService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OAuthKakaoCallback implements OAuthCallbackService {

    @Value("{oauth.kakao.url}")
    private String oauthUrl;
    @Value("{oauth.kakao.id}")
    private String oauthId;
    @Value("{oauth.kakao.redirect}")
    private String redirect;

    private WebClient webClient;

    @PostConstruct
    protected void init () {
        webClient = WebClient.create(oauthUrl);
    }

    @Override
    public OAuthProfile getProfile (String accessToken) {
        return null;
    }

    @Override
    public OAuthToken getAccessToken (String authorizationCode) {
        return webClient
                .post()
                .uri("/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(createBody(authorizationCode)))
                .retrieve()
                .bodyToMono(OAuthToken.class)
                .block();
    }

    private MultiValueMap<String, String> createBody (String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", oauthId);
        body.add("redirect_uri", redirect);
        body.add("code", authorizationCode);
        return body;
    }
}
