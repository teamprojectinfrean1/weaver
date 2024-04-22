package com.task.weaver.domain.issue.dto.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.task.weaver.common.model.Status;

import lombok.Builder;

@Builder
public record UpdateIssueRequest(UUID modifierId,
								 UUID assigneeId,
								 UUID taskId,
								 LocalDateTime startDate,
								 LocalDateTime endDate,
								 String issueTitle,
								 String issueContent) {
}
