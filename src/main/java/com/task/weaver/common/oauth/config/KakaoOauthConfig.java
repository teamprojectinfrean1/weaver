package com.task.weaver.common.oauth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.kakao")
public record KakaoOauthConfig(String redirectUri,
                               String clientId,
                               String clientSecret,
                               String[] scope) {
}
