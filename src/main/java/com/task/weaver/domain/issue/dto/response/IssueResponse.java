package com.task.weaver.domain.issue.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.task.weaver.domain.issue.entity.Issue;

import lombok.Builder;

@Builder
public record IssueResponse(UUID issueId,
							String title,
							String content,
							LocalDateTime modDate,
							String status
							) {

	public IssueResponse(Issue issue) {
		this(issue.getIssueId(), issue.getTitle(), issue.getContent(), issue.getModDate(), issue.getStatus().toString());
	}
}
