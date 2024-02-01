package com.task.weaver.domain.issue.dto.response;

import java.time.LocalDateTime;

import com.task.weaver.domain.issue.entity.Issue;

import lombok.Builder;

@Builder
public record IssueResponse(Long issueId,
							String issueName,
							String issueType,
							LocalDateTime createDate
							) {
	public IssueResponse(Issue issue) {
		this(issue.getIssueId(), issue.getIssueName(), issue.getIssueType(), issue.getCreatedDate());
	}
}
