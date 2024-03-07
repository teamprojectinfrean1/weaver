package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

import java.util.UUID;

@Builder
public record CreateIssueRequest(UUID userId,
								 Long taskId,
								 Long statusTagId,
								 String issueName,
								 String issueType,
								 String issueText) {
}
