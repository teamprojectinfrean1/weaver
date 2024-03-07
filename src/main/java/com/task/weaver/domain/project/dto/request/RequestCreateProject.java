package com.task.weaver.domain.project.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record RequestCreateProject(@NotEmpty String projectName,
                                   @NotEmpty String projectContent,
                                   LocalDateTime startDate,
                                   LocalDateTime endDate,
                                   boolean isPublic,
                                   List<String> projectTagList,
                                   List<UUID> memberUuidList) {
}
