package com.task.weaver.domain.issue.dto.response;

import com.task.weaver.domain.issue.entity.Issue;
import lombok.Builder;

import java.net.URL;
import java.util.UUID;

@Builder
public record GetIssueListResponse(UUID issueId,
								   String issueTitle,
								   String issueStatus,
								   UUID taskId,
								   String taskTitle,
								   UUID assigneeId,
								   String assigneeNickname,
								   URL assigneeProfileImage) {

	public static GetIssueListResponse of(Issue issue) {
		return GetIssueListResponse.builder()
				.issueId(issue.getIssueId())
				.issueTitle(issue.getIssueTitle())
				.issueStatus(String.valueOf(issue.getStatus()))
				.taskId(issue.getTask().getTaskId())
				.taskTitle(issue.getTask().getTaskTitle())
				.assigneeId(issue.getAssignee().getId())
				.assigneeNickname(issue.getAssignee().resolveMemberByLoginType().getNickname())
				.assigneeProfileImage(issue.getAssignee().resolveMemberByLoginType().getProfileImage())
				.build();
	}
}
