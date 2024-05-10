package com.task.weaver.domain.issue.dto.request;

import java.util.Optional;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateIssueRequest(UUID creatorId,
								 Optional<UUID> assigneeId,
								 UUID taskId,
								 Optional<LocalDateTime> startDate,
								 Optional<LocalDateTime> endDate,
								 String issueTitle,
								 Optional<String> issueContent,
								 String status) {
}
