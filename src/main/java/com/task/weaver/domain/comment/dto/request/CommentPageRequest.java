package com.task.weaver.domain.comment.dto.request;

public record CommentPageRequest(
        int page,
        int size,
        Long issueId
) {
}