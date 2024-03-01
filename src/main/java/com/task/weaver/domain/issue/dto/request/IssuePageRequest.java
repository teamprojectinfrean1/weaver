package com.task.weaver.domain.issue.dto.request;

import lombok.Builder;

@Builder
public record IssuePageRequest(int page,
							   int size
							   ) {
}
