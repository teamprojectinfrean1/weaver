package com.task.weaver.domain.comment.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    @Column(name = "create_date")
    private LocalDateTime create_date;

}
