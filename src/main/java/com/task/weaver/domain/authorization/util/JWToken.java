package com.task.weaver.domain.authorization.util;

import lombok.Builder;

@Builder
public record JWToken(String accessToken,
					  String refreshToken) {
}
