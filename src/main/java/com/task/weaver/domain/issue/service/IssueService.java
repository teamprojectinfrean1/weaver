package com.task.weaver.domain.issue.service;

import java.util.UUID;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;

import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {

    IssueResponse getIssue (Long issueId) throws NotFoundException, AuthorizationException;

    Page<GetIssueListResponse> getIssues(String status, GetIssuePageRequest getIssuePageRequest) throws NotFoundException, AuthorizationException;

    Long addIssue(CreateIssueRequest issueRequest) throws AuthorizationException;


    Issue updateIssue (Issue originalIssue, Issue newIssue) throws NotFoundException, AuthorizationException;
    Issue updateIssue (Long originalIssueId, Issue newIssue) throws NotFoundException, AuthorizationException;

    void deleteIssue (Issue issue) throws NotFoundException, AuthorizationException;
    void deleteIssue (Long issueId) throws NotFoundException, AuthorizationException;

}
