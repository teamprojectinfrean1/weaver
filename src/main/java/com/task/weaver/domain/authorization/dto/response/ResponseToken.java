package com.task.weaver.domain.authorization.dto.response;

import lombok.Builder;

@Builder
public record ResponseToken(String accessToken,
							String refreshToken) {
}
