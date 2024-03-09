package com.task.weaver.domain.comment.dto.request;

import java.util.UUID;

public record CommentPageRequest(
        int page,
        int size,
        Long issueId
) {
}
