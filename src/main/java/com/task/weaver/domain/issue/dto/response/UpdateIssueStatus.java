package com.task.weaver.domain.issue.dto.response;

import com.task.weaver.common.model.Status;
import java.util.UUID;

public record UpdateIssueStatus(UUID issueId,
                                Status status) {

    public static UpdateIssueStatus of(UUID issueId, Status status) {
        return new UpdateIssueStatus(issueId, status);
    }
}
