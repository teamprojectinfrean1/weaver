package com.task.weaver.domain.issue.dto.response;

import java.time.LocalDateTime;

import com.task.weaver.domain.issue.entity.Issue;

import lombok.Builder;

@Builder
public record IssueResponse(Long issueId,
							String title,
							String content,
							LocalDateTime modDate,
							String status
							) {

	public IssueResponse(Issue issue) {
		this(issue.getIssueId(), issue.getTitle(), issue.getContent(), issue.getModDate(), issue.getStatus().toString());
	}
}
