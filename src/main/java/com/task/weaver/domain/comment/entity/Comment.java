package com.task.weaver.domain.comment.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.user.entity.User;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "COMMENT")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long comment_id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    private Issue issue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String body;

    @Column(name = "create_date")
    private LocalDateTime date;
    @Nullable
    private String reaction;


    public void updateComment(RequestUpdateComment requestUpdateComment, Issue issue) {
        this.issue = issue;
        this.body = requestUpdateComment.getCommentBody();
        this.date = requestUpdateComment.getDate();
    }
    public void updateReaction(String reaction){
        this.reaction = reaction;
    }
}