package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

@Builder
public record IssueRequest(Long issueId,
						   Long userId,
						   Long taskId,
						   String issueName,
						   String issueType,
						   String issueText) {
}
