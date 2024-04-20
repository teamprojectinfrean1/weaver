package com.task.weaver.domain.member.dto.response;

import lombok.Builder;

@Builder
public record ResponseToken(String accessToken,
							String refreshToken) {
}
