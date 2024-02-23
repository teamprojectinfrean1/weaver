package com.task.weaver.domain.issue.entity;

import com.task.weaver.domain.issue.dto.request.IssueRequest;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

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
    @JoinColumn(name = "create_user_id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "status_tag_id")
    private StatusTag statusTag;

    @Column(name = "issue_name", length = 100)
    private String issueName;

    @Column(name = "issue_type", length = 100)
    private String issueType;

    @Column(name = "issue_text")
    private String issueText;

    @CreatedDate
    private LocalDateTime createdDate;

    public static Issue from(IssueRequest issueRequest, Task task, User user, StatusTag statusTag) {
        return Issue.builder().task(task).user(user).statusTag(statusTag).issueName(issueRequest.issueName()).issueType(
            issueRequest.issueType()).issueText(issueRequest.issueText()).build();
    }
}
