package com.task.weaver.domain.project.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record RequestCreateProject(Long projectId,
                                   @NotEmpty String customUrl,
                                   @NotEmpty String bannerUrl,
                                   @NotEmpty String name,
                                   @NotEmpty String detail,
                                   LocalDateTime dueDate,
                                   LocalDateTime created,
                                   boolean hasPublic) {
}
