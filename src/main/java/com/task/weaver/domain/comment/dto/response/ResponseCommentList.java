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
    private String body;
    private String userNickname;
    private URL userProfileImage;
    private LocalDateTime updatedAt;

    public ResponseCommentList(Comment comment){
        this.commentId = comment.getComment_id();
        this.body = comment.getBody();
        this.userId = comment.getIssue().getIssueId();
        this.userNickname = comment.getUser().getNickname();
        this.userProfileImage = comment.getUser().getProfileImage();
        this.updatedAt = comment.getModDate();
    }
}
