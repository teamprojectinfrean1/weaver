package com.task.weaver.domain.project.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Builder;

@Builder
public record RequestCreateProject(@NotEmpty String projectName,
                                   @NotEmpty String projectContent,
                                   @NotEmpty UUID writerUuid,
                                   LocalDateTime startDate,
                                   LocalDateTime endDate,
                                   LocalDateTime createDate,
                                   List<String> projectTagList,
                                   List<UUID> memberUuidList) {
}
