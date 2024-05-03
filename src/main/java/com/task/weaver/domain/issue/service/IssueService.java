package com.task.weaver.domain.issue.service;

import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import java.util.UUID;

public interface IssueService {

    IssueResponse getIssueResponse(UUID issueId);

    ResponsePageResult<GetIssueListResponse, Issue> getIssues(Status status, GetIssuePageRequest getIssuePageRequest);

    IssueResponse addIssue(CreateIssueRequest issueRequest);


    IssueResponse updateIssue(UUID issueId, UpdateIssueRequest updateIssueRequest);

    void updateIssueStatus(UUID issueId, String status);

    void deleteIssue(Issue issue);

    void deleteIssue(UUID issueId);

    ResponsePageResult<GetIssueListResponse, Issue> getSearchIssues(String status, String filter, String word,
                                                                    GetIssuePageRequest getIssuePageRequest);
}
