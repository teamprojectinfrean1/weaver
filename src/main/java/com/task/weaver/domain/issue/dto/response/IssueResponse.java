package com.task.weaver.domain.issue.dto.response;

import lombok.Builder;

@Builder
public record IssueResponse(Long issueId,
							String issueName) {
}
