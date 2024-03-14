package com.task.weaver.domain.project.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
public record RequestUpdateProject (@NotEmpty String projectName,
                                    @NotEmpty String projectContent,
                                    @NotEmpty UUID updaterUuid,
                                    LocalDateTime startDate,
                                    LocalDateTime endDate,
                                    List<String> projectTagList,
                                    List<UUID> memberUuidList){
}
