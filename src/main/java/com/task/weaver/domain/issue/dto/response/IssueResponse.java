package com.task.weaver.domain.issue.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record IssueResponse(Long issueId,
							String title,
							String content,
							LocalDateTime modDate
							) {

	// public IssueResponse(Issue issue) {
	// 	this(issue.getIssueId(), issue.getIssueName(), issue.getIssueType(), issue.getCreatedDate(), issue.getStatusTag());
	// }
}
