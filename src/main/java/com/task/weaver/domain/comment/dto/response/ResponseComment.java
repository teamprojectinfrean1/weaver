package com.task.weaver.domain.comment.dto.response;

import com.task.weaver.domain.comment.entity.Comment;
import lombok.Builder;

@Builder
public record ResponseComment(
        Long commentId,
        String body
) {
    public ResponseComment(Comment comment) {
        this(comment.getComment_id(),comment.getBody());
    }
}
