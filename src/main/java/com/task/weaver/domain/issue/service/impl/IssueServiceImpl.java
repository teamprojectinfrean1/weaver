package com.task.weaver.domain.issue.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.Task;
import com.task.weaver.domain.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IssueServiceImpl implements IssueService {

	@Override
	public Issue getIssue(Long issueId) throws NotFoundException, AuthorizationException {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Long taskId, Pageable pageable) throws NotFoundException, AuthorizationException {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Task task, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Task task, Long userId, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Task task, User user, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Long taskId, Long userId, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Issue> getIssues(Long taskId, User user, Pageable pageable) {
		return null;
	}

	@Override
	public Page<Issue> getIssues(StatusTag statusTag, Pageable pageable) {
		return null;
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

	}

	@Override
	public void deleteIssue(Long issueId) throws NotFoundException, AuthorizationException {

	}
}
