package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

@Builder
public record CreateIssueRequest(Long issueId,
								 Long userId,
								 Long taskId,
								 Long statusTagId,
								 String issueName,
								 String issueType,
								 String issueText) {
}
