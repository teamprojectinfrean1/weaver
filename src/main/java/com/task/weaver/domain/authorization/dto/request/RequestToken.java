package com.task.weaver.domain.authorization.dto.request;

import lombok.Builder;

@Builder
public record RequestToken(String accessToken,
						   String refreshToken) {
}
