package com.task.weaver.domain.authorization.dto;

public record OAuthProfile(
        String uid,
        String avatar,
        String email,
        String name
) {
}
