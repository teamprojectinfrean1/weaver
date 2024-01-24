package com.task.weaver.domain.project.dto.request;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record RequestCreateProject(Long projectId,
                                   String customUrl,
                                   String bannerUrl,
                                   String name,
                                   String detail,
                                   LocalDateTime dueDate,
                                   LocalDateTime created,
                                   boolean hasPublic) {
}
