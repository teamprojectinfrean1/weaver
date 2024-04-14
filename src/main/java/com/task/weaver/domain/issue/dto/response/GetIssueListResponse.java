package com.task.weaver.domain.issue.dto.response;

import lombok.Builder;

import java.net.URL;
import java.util.UUID;

@Builder
public record GetIssueListResponse(UUID issueId,
								   String issueTitle,
								   UUID taskId,
								   String taskTitle,
								   String assigneeId,
								   String assigneeNickname,
								   URL assigneeProfileImage) {
	// public GetIssueListResponse(Issue issue) {
	// 	this.issueId = issue.getIssueId();
	// 	this.issueTitle = issue.getIssueTitle();
	// 	this.taskId = issue.getTask().getTaskId();
	// 	this.taskTitle = issue.getTask().getTaskTitle();
	// 	this.assigneeId = issue.getAssignee().getId();
	// 	this.assigneeNickname = issue.getAssignee().getNickname();
	// 	this.assigneeProfileImage = issue.getAssignee().getProfileImage();
	// }
}
