package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateIssueRequest(UUID creatorId,
								 UUID assigneeId,
								 UUID taskId,
								 LocalDateTime startDate,
								 LocalDateTime endDate,
								 String issueTitle,
								 String issueContent,
								 String status) {
}
