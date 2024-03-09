package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateIssueRequest(UUID creatorId,
								 UUID managerId,
								 Long taskId,
								 LocalDateTime startDate,
								 LocalDateTime endDate,
								 String title,
								 String content,
								 String status) {
}
