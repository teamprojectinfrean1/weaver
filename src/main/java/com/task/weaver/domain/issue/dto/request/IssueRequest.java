package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public record IssueRequest(Long issueId,
						   Long userId,
						   Long taskId,
						   Long statusTagId,
						   String issueName,
						   String issueType,
						   String issueText) {
}
