package com.task.weaver.domain.issue.entity;

import com.task.weaver.common.model.Status;
import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Issue extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "issue_id")
    private UUID issueId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "content")
    private String content;

    // @LastModifiedDate
    // @Column(name = "modify_date")
    // private LocalDateTime modDate;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void updateIssue(UpdateIssueRequest updateIssueRequest, Task task, User modifier, User manager) {
        this.task = task;
        this.creator = modifier;
        this.manager = manager;
        this.title = updateIssueRequest.title();
        this.content = updateIssueRequest.content();
        this.startDate = updateIssueRequest.startDate();
        this.endDate = updateIssueRequest.endDate();
        this.status = Status.valueOf(updateIssueRequest.status());
    }
}
