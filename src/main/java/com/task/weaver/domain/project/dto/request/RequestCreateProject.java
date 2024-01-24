package com.task.weaver.domain.project.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public record ProjectDTO(Long projectId,
                         String customUrl,
                         String bannerUrl,
                         String name,
                         String detail,
                         LocalDateTime dueDate,
                         LocalDateTime created,
                         boolean isPublic) {
}
