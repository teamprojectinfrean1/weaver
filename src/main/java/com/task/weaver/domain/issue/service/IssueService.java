package com.task.weaver.domain.issue.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {

    IssueResponse getIssue (Long issueId) throws NotFoundException, AuthorizationException;

    Page<Issue> getIssues (Long taskId, Pageable pageable) throws NotFoundException, AuthorizationException;
    Page<Issue> getIssues (Long taskId, Long userId, Pageable pageable) throws NotFoundException, AuthorizationException;


    Long addIssue(CreateIssueRequest issueRequest) throws AuthorizationException;

    Issue addIssue (Issue issue, Long taskId, Long userId) throws AuthorizationException;

    Issue updateIssue (Issue originalIssue, Issue newIssue) throws NotFoundException, AuthorizationException;
    Issue updateIssue (Long originalIssueId, Issue newIssue) throws NotFoundException, AuthorizationException;

    void deleteIssue (Issue issue) throws NotFoundException, AuthorizationException;
    void deleteIssue (Long issueId) throws NotFoundException, AuthorizationException;

}
