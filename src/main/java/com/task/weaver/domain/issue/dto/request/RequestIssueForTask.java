package com.task.weaver.domain.issue.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestIssueForTask {
    private String issueId;
    private String issueName;
}
