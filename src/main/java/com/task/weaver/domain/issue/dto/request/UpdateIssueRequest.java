package com.task.weaver.domain.issue.dto.request;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.task.weaver.common.model.Status;

import lombok.Builder;

@Builder
public record UpdateIssueRequest(UUID modifierId,
								 UUID assigneeId,
								 UUID taskId,
								 Optional<LocalDateTime> startDate,
								 Optional<LocalDateTime> endDate,
								 String issueTitle,
								 Optional<String> issueContent,
								 String status) {
}
