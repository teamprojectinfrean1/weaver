package com.task.weaver.domain.issue.dto.response;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
		this.assigneeId = Optional.ofNullable(issue.getAssignee()).map(Member::getId).orElse(null);
		this.assigneeNickname = Optional.ofNullable(issue.getAssignee()).map(member -> member.resolveMemberByLoginType().getNickname()).orElse(null);
		this.assigneeProfileImage = Optional.ofNullable(issue.getAssignee()).map(member -> member.resolveMemberByLoginType().getProfileImage()).orElse(null);
		this.startDate = issue.getStartDate();
		this.endDate = issue.getEndDate();
		this.lastUpdateDetail = ResponseUpdateDetail.builder()
				.memberUuid(issue.getModifier().getId())
				.userNickname(issue.getModifier().resolveMemberByLoginType().getNickname())
				.updatedDate(issue.getModDate())
				.build();
	}
}