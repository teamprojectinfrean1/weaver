package com.task.weaver.domain.issue.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.dto.request.IssueRequest;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.entity.IssueMention;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;

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
	// private final UserRepository userRepository;
	// private final TaskRepository taskRepository;

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

//	@Override
//	public Page<Issue> getIssues(Long taskId, Pageable pageable) throws NotFoundException, AuthorizationException {
//		return issueRepository.findAllBytaskId(taskId, pageable);
//	}

	@Override
	public Page<Issue> getIssues(Task task, Pageable pageable) throws NotFoundException, AuthorizationException {
		return issueRepository.findAllByTask(task, pageable);
	}

	@Override
	public Page<Issue> getIssues(Task task, Long userId, Pageable pageable) throws
		NotFoundException,
		AuthorizationException {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Task task, User user, Pageable pageable) throws
		NotFoundException,
		AuthorizationException {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Long taskId, Long userId, Pageable pageable) throws
		NotFoundException,
		AuthorizationException {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Long taskId, User user, Pageable pageable) throws
		NotFoundException,
		AuthorizationException {
		return null;
	}

	@Override
	public Page<Issue> getIssues(StatusTag statusTag, Pageable pageable) throws
		NotFoundException,
		AuthorizationException {
		return null;
	}

	@Override
	public Page<Issue> getIssues(IssueMention issueMention, Pageable pageable) throws
		NotFoundException,
		AuthorizationException {
		return null;
	}

	@Override
	public void addIssue(IssueRequest issueRequest) throws AuthorizationException {
		// task, user, statustag
		// Task task = taskRepository.findById(issueRequest.getTaskId())
		// 	.orElseThrow(() -> new IllegalArgumentException(""));
		// User user = userRepository.findById(issueRequest.getUserId())
		// 	.orElseThrow(() -> new IllegalArgumentException(""));

		// Issue issue = Issue.from(issueRequest, task, user, statusTag);
		// issueRepository.save(issue);
	}

	@Override
	public Issue addIssue(Issue issue, Task task, User user) throws AuthorizationException {
		return null;
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
