package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateIssueRequest(UUID creatorId,
								 UUID managerId,
								 UUID taskId,
								 String startDate,
								 String endDate,
								 String title,
								 String content,
								 String status) {
}
