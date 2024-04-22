package com.task.weaver.domain.member.dto.request;

import lombok.Builder;

@Builder
public record RequestSignIn(String id,
							String password) {
}
