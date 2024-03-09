package com.task.weaver.domain.comment.dto.response;

import com.task.weaver.domain.comment.entity.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ResponseComment(
        Long commentId,
        String body,
        LocalDateTime date
) {
    public ResponseComment(Comment comment) {
       this(comment.getComment_id(),comment.getBody(),comment.getDate());
    }
}
