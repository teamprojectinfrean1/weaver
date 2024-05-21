package com.task.weaver.domain.member.dto.response;

import lombok.Builder;

@Builder
public record ResponseToken(String accessToken,
							String refreshToken) {

	public static ResponseToken of(String accessToken, String refreshToken) {
		return ResponseToken.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}
}
