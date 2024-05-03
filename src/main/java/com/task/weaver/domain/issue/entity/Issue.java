package com.task.weaver.domain.issue.entity;

import com.task.weaver.common.model.Status;
import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.task.entity.Task;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Table(name = "ISSUE")
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
    @JoinColumn(name = "modifier_id")
    private Member modifier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private Member assignee;

    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Column(name = "issue_title", length = 100)
    private String issueTitle;

    @Column(name = "issue_content")
    private String issueContent;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private Status status;

    public void updateIssue(UpdateIssueRequest updateIssueRequest, Task task, Member modifier, Member assignee) {
        this.task = task;
        this.modifier = modifier;
        this.assignee = assignee;
        this.issueTitle = updateIssueRequest.issueTitle();
        this.issueContent = updateIssueRequest.issueContent();
        this.startDate = updateIssueRequest.startDate();
        this.endDate = updateIssueRequest.endDate();
    }

    public boolean hasIssueProgress() {
        return status == Status.INPROGRESS;
    }

    public void updateTask(Task task) {
        this.task = task;
    }

    public void updateModifier(Member modifier) {
        this.modifier = modifier;
    }

    public void updateAssignee(Member assignee) {
        this.assignee = assignee;
    }

    public void updateIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public void updateIssueContent(String issueContent) {
        this.issueContent = issueContent;
    }

    public void updateStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}

