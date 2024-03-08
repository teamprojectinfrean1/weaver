package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

@Builder
public record CreateIssueRequest(Long creatorId,
								 Long managerId,
								 Long taskId,
								 String startDate,
								 String endDate,
								 String title,
								 String content,
								 String status) {
}
