package com.task.weaver.domain.comment.dto.response;

import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseComment {
    private Long commentId;
    private String commentBody;


    public ResponseComment (Comment comment){
        this.commentId = comment.getComment_id();
        this.commentBody = comment.getBody();
    }
    public ResponseComment (RequestUpdateComment requestUpdateComment){
        this.commentBody = requestUpdateComment.getCommentBody();
    }
}
