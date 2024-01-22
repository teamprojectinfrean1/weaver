package com.task.weaver.domain.issue;

import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.Task;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_tag_id")
    private StatusTag statusTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_user_id")
    private User user;

    @Column(name = "issue_name", length = 100)
    private String issueName;

    @Column(name = "issue_type", length = 100)
    private String issueType;

    @Column(name = "issue_text")
    private String issueText;

    private LocalDateTime created;
}
