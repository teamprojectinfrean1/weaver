package com.task.weaver.domain.issue.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Builder
// public record IssueResponse(
// 		UUID projectId,
// 		UUID taskId,
// 		String taskTitle,
// 		UUID issueId,
// 		String issueTitle,
// 		String issueContent,
// 		String status,
// 		UUID assigneeId,
// 		String assigneeNickname,
// 		String assigneeProfileImage,
// 		LocalDateTime startDate,
// 		LocalDateTime endDate,
// 		ResponseUpdateDetail lastUpdateDetail
// 		) {
//
// 	public IssueResponse(Issue issue, User assignee, User modifier) {
// 		this(issue.getIssueId(), issue.getTitle(), issue.getContent(), issue.getModDate(), issue.getStatus().toString());
// 	}
// }

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponse {
	private UUID projectId;
	private UUID taskId;
	private String taskTitle;
	private UUID issueId;
	private String issueTitle;
	private String issueContent;
	private String status;
	private UUID assigneeId;
	private String assigneeNickname;
	private String assigneeProfileImage;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private ResponseUpdateDetail lastUpdateDetail;

	public IssueResponse(Issue issue) {
		this.projectId = issue.getTask().getProject().getProjectId();
		this.taskId = issue.getTask().getTaskId();
		this.taskTitle = issue.getTask().getTaskTitle();
		this.issueId = issue.getIssueId();
		this.issueTitle = issue.getIssueTitle();
		this.issueContent = issue.getIssueContent();
		this.status = issue.getStatus().toString();
		this.assigneeId = issue.getAssignee().getUserId();
		this.assigneeNickname = issue.getAssignee().getNickname();
		this.assigneeProfileImage = issue.getAssignee().getProfileImage();
		this.startDate = issue.getStartDate();
		this.endDate = issue.getEndDate();
		this.lastUpdateDetail = ResponseUpdateDetail.builder()
			.userUuid(issue.getModifier().getUserId())
			.userNickname(issue.getModifier().getNickname())
			.updatedDate(issue.getModDate())
			.build();
	}
}