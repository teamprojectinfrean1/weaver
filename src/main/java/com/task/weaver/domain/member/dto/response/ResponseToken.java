package com.task.weaver.domain.member.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public record ResponseToken(String accessToken,
							String refreshToken) {

	public static ResponseToken of(String accessToken, String refreshToken) {
		return ResponseToken.builder()
				.accessToken(accessToken)
				.refreshToken(refreshToken)
				.build();
	}
}
