package com.task.weaver.domain.authorization.dto.request;

import lombok.Builder;

@Builder
public record RequestSignIn(String email,
							String password) {
}
