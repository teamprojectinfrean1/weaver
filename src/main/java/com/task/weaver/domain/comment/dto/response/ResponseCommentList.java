package com.task.weaver.domain.comment.dto.response;

import com.task.weaver.domain.comment.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseCommentList {
    private UUID commentId;
    private UUID userId;
    private String commentBody;
    private URL userProfileImage;
    private String userNickname;
    private LocalDateTime updatedAt;

    public ResponseCommentList(Comment comment){
        this.commentId = comment.getCommentId();
        this.commentBody = comment.getBody();
        this.userId = comment.getIssue().getModifier().getId();
        this.userProfileImage = comment.getIssue().getModifier().getUser().getProfileImage();
        this.userNickname = comment.getIssue().getModifier().getUser().getNickname();
        this.updatedAt = comment.getModDate();
    }
}
