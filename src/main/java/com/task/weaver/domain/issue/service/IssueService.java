package com.task.weaver.domain.issue.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.entity.IssueMention;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.Task;
import com.task.weaver.domain.user.entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface IssueService {

    IssueResponse getIssue (Long issueId) throws NotFoundException, AuthorizationException;

    Page<Issue> getIssues (Long taskId, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (Task task, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (Task task, Long userId, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (Task task, User user, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (Long taskId, Long userId, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (Long taskId, User user, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (StatusTag statusTag, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (IssueMention issueMention, Pageable pageable) throws NotFoundException, AuthorizationException;


    Issue addIssue (Issue issue, Task task, User user) throws AuthorizationException;
    Issue addIssue (Issue issue, Long taskId, Long userId) throws AuthorizationException;

    Issue updateIssue (Issue originalIssue, Issue newIssue) throws NotFoundException, AuthorizationException;
    Issue updateIssue (Long originalIssueId, Issue newIssue) throws NotFoundException, AuthorizationException;

    void deleteIssue (Issue issue) throws NotFoundException, AuthorizationException;
    void deleteIssue (Long issueId) throws NotFoundException, AuthorizationException;

}
