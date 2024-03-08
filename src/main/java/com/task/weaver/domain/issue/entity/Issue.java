package com.task.weaver.domain.issue.entity;

import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

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
    private Task taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private User writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User managerId;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "issue_text")
    private String issueText;

    @LastModifiedDate
    private LocalDateTime updateDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "visible")
    private boolean visible;

    public static Issue from(CreateIssueRequest issueRequest, Task task, User user) {
        return Issue.builder().task(task).writerId(user).managerId(user).title(issueRequest.issueName()).issueType(
            issueRequest.issueType()).issueText(issueRequest.issueText()).build();
    }
}
