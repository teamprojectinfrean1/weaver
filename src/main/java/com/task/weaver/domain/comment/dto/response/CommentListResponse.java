package com.task.weaver.domain.comment.dto.response;

public record CommentListResponse(
        Long commentId,
        String body,
        Long issueId
) {
}
