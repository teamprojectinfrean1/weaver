package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateIssueRequest(UUID userId,
								 Long managerId,
								 Long taskId,
								 String startDate,
								 String endDate,
								 String title,
								 String content,
								 String status) {
}
