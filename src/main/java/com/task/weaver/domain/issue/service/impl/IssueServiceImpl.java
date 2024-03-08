package com.task.weaver.domain.issue.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IssueServiceImpl implements IssueService {
	private final IssueRepository issueRepository;
	private final UserRepository userRepository;
	private final TaskRepository taskRepository;

	@Override
	public IssueResponse getIssue(Long issueId) throws NotFoundException, AuthorizationException {
		Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException(""));
		return new IssueResponse(issue);
	}

	@Override
	public Page<Issue> getIssues(final Long taskId, final Pageable pageable)
			throws NotFoundException, AuthorizationException {
		return null;
	}

	// @Override
	// public Page<Issue> getIssues(Long taskId, Pageable pageable) throws NotFoundException, AuthorizationException {
	// 	return issueRepository.findAllBytaskId(taskId, pageable);
	// }

	@Override
	public Page<Issue> getIssues(Long taskId, Long userId, Pageable pageable) throws
		NotFoundException,
		AuthorizationException {
		return null;
	}

	@Override
	public Long addIssue(CreateIssueRequest createIssueRequest) throws AuthorizationException {
		Task task = taskRepository.findById(createIssueRequest.taskId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		User creator = userRepository.findById(createIssueRequest.creatorId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		User manager = userRepository.findById(createIssueRequest.managerId())
			.orElseThrow(() -> new IllegalArgumentException(""));

		Issue issue = Issue.builder()
			.task(task)
			.creator(creator)
			.manager(manager)
			.title(createIssueRequest.title())
			.content(createIssueRequest.content())
			.startDate(LocalDateTime.parse(createIssueRequest.startDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
			.endDate(LocalDateTime.parse(createIssueRequest.endDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
			.visible(false)
			.status(Status.valueOf(createIssueRequest.status()))
			.build();
		return issueRepository.save(issue).getIssueId();
	}

	@Override
	public Issue addIssue(Issue issue, Long taskId, Long userId) throws AuthorizationException {
		return null;
	}

	@Override
	public Issue updateIssue(Issue originalIssue, Issue newIssue) throws NotFoundException, AuthorizationException {
		return null;
	}

	@Override
	public Issue updateIssue(Long originalIssueId, Issue newIssue) throws NotFoundException, AuthorizationException {
		return null;
	}

	@Override
	public void deleteIssue(Issue issue) throws NotFoundException, AuthorizationException {
		issueRepository.delete(issue);
	}

	@Override
	public void deleteIssue(Long issueId) throws NotFoundException, AuthorizationException {
		issueRepository.deleteById(issueId);
	}
}
