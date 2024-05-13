package com.task.weaver.domain.issue.dto.request;

import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.task.entity.Task;
import java.time.LocalDate;
import java.util.Optional;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateIssueRequest(UUID creatorId,
								 Optional<UUID> assigneeId,
								 UUID taskId,
								 Optional<LocalDateTime> startDate,
								 Optional<LocalDateTime> endDate,
								 String issueTitle,
								 Optional<String> issueContent,
								 String status) {

	public static Issue dtoToEntity(Task task, Member creator, Member assignee, CreateIssueRequest createIssueRequest) {
		return Issue.builder()
				.task(task)
				.modifier(creator) // 생성자 & 수정자 역할
				.assignee(assignee) // 담당자
				.issueTitle(createIssueRequest.issueTitle())
				.issueContent(createIssueRequest.issueContent().orElse("NO_ISSUE_CONTENT"))
				.startDate(createIssueRequest.startDate().orElse(null))
				.endDate(createIssueRequest.endDate().orElse(null))
				.status(Status.fromName(createIssueRequest.status()))
				.build();
	}
}
