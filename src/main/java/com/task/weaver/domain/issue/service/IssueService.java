package com.task.weaver.domain.issue.service;

import java.util.UUID;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;

import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueService {

    IssueResponse getIssue (UUID issueId) throws NotFoundException, AuthorizationException;

    Page<GetIssueListResponse> getIssues(String status, GetIssuePageRequest getIssuePageRequest) throws NotFoundException, AuthorizationException;

    UUID addIssue(CreateIssueRequest issueRequest) throws AuthorizationException;


    IssueResponse updateIssue (UUID issueId, UpdateIssueRequest updateIssueRequest) throws NotFoundException, AuthorizationException;

    void deleteIssue (Issue issue) throws NotFoundException, AuthorizationException;
    void deleteIssue (UUID issueId) throws NotFoundException, AuthorizationException;

    Page<GetIssueListResponse> getSearchIssues(String status, String filter, String word, GetIssuePageRequest getIssuePageRequest) throws NotFoundException, AuthorizationException;
}
