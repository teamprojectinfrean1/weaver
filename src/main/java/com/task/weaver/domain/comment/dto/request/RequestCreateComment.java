package com.task.weaver.domain.comment.dto.request;
public record RequestCreateComment
        (
                Long commentId,
                Long writerUserId,
                Long mentionTaskId,
                String body
        ) {
}