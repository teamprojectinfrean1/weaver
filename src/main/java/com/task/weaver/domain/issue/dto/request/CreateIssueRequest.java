package com.task.weaver.domain.issue.dto.request;

import com.task.weaver.domain.issue.entity.IssueStatus;
import com.task.weaver.domain.issue.service.IssueService;

import lombok.Builder;

@Builder
public record CreateIssueRequest(Long creatorId,
								 Long managerId,
								 Long taskId,
								 String startDate,
								 String endDate,
								 String title,
								 String content,
								 Long statusId) {
}
