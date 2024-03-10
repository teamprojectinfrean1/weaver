package com.task.weaver.domain.issue.dto.response;

import java.util.UUID;

import lombok.Builder;

@Builder
public record GetIssueListResponse(UUID issueId,
								   String issueTitle,
								   UUID taskId,
								   String taskTitle,
								   String managerId,
								   String managerNickname,
								   String managerProfileImage) {
}
