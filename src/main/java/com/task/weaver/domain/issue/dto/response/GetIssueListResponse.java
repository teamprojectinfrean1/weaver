package com.task.weaver.domain.issue.dto.response;

import lombok.Builder;

@Builder
public record GetIssueListResponse(Long issueId,
								   String issueTitle,
								   Long taskId,
								   String taskTitle,
								   String managerId,
								   String managerNickname,
								   String managerProfileImage) {
}
