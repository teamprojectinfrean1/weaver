package com.task.weaver.domain.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record EmailCheckDto(@NotEmpty @Email String email,
                            @NotEmpty String verificationCode) {
}
