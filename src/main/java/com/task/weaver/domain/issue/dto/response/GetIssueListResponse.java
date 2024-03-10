package com.task.weaver.domain.issue.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record GetIssueListResponse(Long issueId,
								   String issueTitle,
								   UUID taskId,
								   String taskTitle,
								   String managerId,
								   String managerNickname,
								   String managerProfileImage) {
}
