package com.task.weaver.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "oauth.google")
public record GoogleOauthConfig(
        String redirectUri,
        String clientId,
        String clientSecret,
        String[] scope
) {
}
