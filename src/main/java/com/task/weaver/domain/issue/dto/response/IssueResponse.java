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
// public record IssueResponse(UUID issueId,
// 							String title,
// 							String content,
// 							LocalDateTime modDate,
// 							String status
// 							) {
//
// 	public IssueResponse(Issue issue) {
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

	public IssueResponse(Issue issue, Task task, User assignee) {
		this.projectId = task.getProject().getProjectId();
		this.taskId = task.getTaskId();
		this.taskTitle = task.getTaskTitle();
		this.issueId = issue.getIssueId();
		this.issueTitle = issue.getIssueTitle();
		this.issueContent = issue.getIssueContent();
		this.status = issue.getStatus().toString();
		this.assigneeId = assignee.getUserId();
		this.assigneeNickname = assignee.getNickname();
		this.assigneeProfileImage = assignee.getProfileImage();
		this.startDate = issue.getStartDate();
		this.endDate = issue.getEndDate();
	}
}