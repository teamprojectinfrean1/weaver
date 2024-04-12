package com.task.weaver.domain.issue.dto.response;

import java.net.URL;
import java.util.UUID;

import lombok.Builder;

import com.task.weaver.domain.issue.entity.Issue;

@Builder
public record GetIssueListResponse(UUID issueId,
								   String issueTitle,
								   UUID taskId,
								   String taskTitle,
								   String assigneeId,
								   String assigneeNickname,
								   String assigneeProfileImage) {
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
