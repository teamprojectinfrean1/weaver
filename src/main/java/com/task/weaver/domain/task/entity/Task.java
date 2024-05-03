package com.task.weaver.domain.task.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import jakarta.persistence.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task")
@Getter
@Setter
@Builder
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "task_id")
    private UUID taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_member_id")
    private Member member;

    @OneToMany(mappedBy = "task")
    @Builder.Default
    private List<Issue> issueList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "last_update_member_id")
    private Member modifier;

    @Column(name = "task_title", length = 100)
    private String taskTitle;

    @Column(name = "task_content")
    private String taskContent;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "status")
    private String status;

    @Column(name = "edit_delete_permission")
    private String editDeletePermission;

    @Column(name = "task_tag")
    private String tags;

    public String getTitle() {
        return this.taskTitle;
    }

    public void updateTask(Task newTask) {
        this.project = newTask.getProject();
        this.member = newTask.getMember();
        this.taskTitle = newTask.getTitle();
        this.taskContent = newTask.getTaskContent();
        this.startDate = newTask.getStartDate();
        this.endDate = newTask.getEndDate();
    }

    public void updateTask(RequestUpdateTask requestUpdateTask, Member updater) {
        this.taskTitle = requestUpdateTask.getTaskTitle();
        this.taskContent = requestUpdateTask.getTaskContent();
        this.tags = String.join(",", requestUpdateTask.getTaskTagList());
        this.startDate = requestUpdateTask.getStartDate().atStartOfDay();
        this.endDate = requestUpdateTask.getEndDate().atStartOfDay();
        this.modifier = updater;
        this.editDeletePermission = requestUpdateTask.getEditDeletePermission();
    }

    public void addIssue(final Issue issue) {
        this.issueList.add(issue);
    }

    public List<Issue> getIssueList() {
        return issueList;
    }

    public List<Issue> getIssuesAsync(Task instance, Executor executor) {
        CompletableFuture<List<Issue>> listCompletableFuture = CompletableFuture.supplyAsync(instance::getIssueList, executor);
        return listCompletableFuture.join();
    }
}