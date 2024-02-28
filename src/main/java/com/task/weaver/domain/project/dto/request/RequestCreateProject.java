package com.task.weaver.domain.project.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record RequestCreateProject(Long projectId,
                                   @NotEmpty String customUrl,
                                   @NotEmpty String bannerUrl,
                                   @NotEmpty String name,
                                   @NotEmpty String detail,
                                   LocalDateTime start_date,
                                   LocalDateTime end_date,
                                   LocalDateTime created,
                                   boolean hasPublic,
                                   List<String> nicknames) {
}
