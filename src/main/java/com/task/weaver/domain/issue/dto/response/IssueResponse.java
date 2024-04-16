package com.task.weaver.domain.issue.dto.response;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

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
	private URL assigneeProfileImage;
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
		this.assigneeId = issue.getAssignee().getId();
		this.assigneeNickname = issue.getAssignee().getUser() != null ? issue.getAssignee().getUser().getNickname() : issue.getAssignee().getOauthMember().nickname();
		this.assigneeProfileImage = issue.getAssignee().getUser() != null ? issue.getAssignee().getUser().getProfileImage() : issue.getAssignee().getOauthMember().getProfileImage();
		this.startDate = issue.getStartDate();
		this.endDate = issue.getEndDate();
		this.lastUpdateDetail = ResponseUpdateDetail.builder()
			.userUuid(issue.getModifier().getId())
			.userNickname(issue.getModifier().getUser() != null ? issue.getModifier().getUser().getNickname() : issue.getModifier().getOauthMember().nickname())
			.updatedDate(issue.getModDate())
			.build();
	}
}