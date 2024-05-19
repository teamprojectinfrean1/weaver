package com.task.weaver.domain.issue.dto.request;

import java.util.UUID;

public record UpdateIssueStatusRequest(String status,
                                       UUID memberId) {
}
