package com.task.weaver.domain.authorization.dto;

public record OAuthToken(String accessToken,
                         String refreshToken) {
}
