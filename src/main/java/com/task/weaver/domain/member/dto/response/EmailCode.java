package com.task.weaver.domain.member.dto.response;

import lombok.Builder;

@Builder
public record EmailCode(boolean isSuccess) {
}
