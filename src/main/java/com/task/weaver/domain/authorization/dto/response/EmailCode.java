package com.task.weaver.domain.authorization.dto.response;

import lombok.Builder;

@Builder
public record EmailCode(boolean isSuccess) {
}
