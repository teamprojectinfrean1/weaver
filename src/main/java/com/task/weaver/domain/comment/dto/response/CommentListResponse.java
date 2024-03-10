package com.task.weaver.domain.comment.dto.response;

import java.util.UUID;

public record CommentListResponse(
        Long commentId,
        String body,
        UUID issueId
) {
}