package com.task.weaver.domain.comment.dto.response;

import java.util.UUID;

public record CommentListResponse(
        UUID commentId,
        String body,
        UUID issueId
) {
}