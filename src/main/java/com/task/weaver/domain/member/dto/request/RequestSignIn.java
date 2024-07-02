package com.task.weaver.domain.member.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record RequestSignIn(@NotEmpty String id,
							@NotEmpty String password) {
}
