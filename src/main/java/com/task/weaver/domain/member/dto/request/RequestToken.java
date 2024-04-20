package com.task.weaver.domain.member.dto.request;

import lombok.Builder;

@Builder
public record RequestToken(String accessToken,
						   String refreshToken) {
}
