package com.task.weaver.domain.task.entity;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "status_tag_id")
    private StatusTag statusTag;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User user;
    @OneToMany(mappedBy = "task")
    @Builder.Default
    private List<Issue> issueList = new ArrayList<>();

    @Column(name = "title", length = 100)
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "start_date")
    private LocalDateTime start_date;
    @Column(name = "end_date")
    private LocalDateTime end_date;

    public String getTitle() {
        return this.title;
    }

    public Task(Long taskId, Project project, StatusTag statusTag, User user, String taskName, String detail, LocalDateTime start_date, LocalDateTime end_date) {
        this.taskId = taskId;
        this.project = project;
        this.statusTag = statusTag;
        this.user = user;
        this.title = taskName;
        this.content = detail;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public void updateTask(Task newTask) {
        this.project = newTask.getProject();
        this.statusTag = newTask.getStatusTag();
        this.user = newTask.getUser();
        this.title = newTask.getTitle();
        this.content = newTask.getContent();
        this.start_date = newTask.getStart_date();
        this.end_date = newTask.getEnd_date();
    }

    public void updateTask(RequestUpdateTask requestUpdateTask) {
        this.project = requestUpdateTask.getProject();
        this.statusTag = requestUpdateTask.getStatusTag();
        this.user = requestUpdateTask.getUser();
        this.title = requestUpdateTask.getTitle();
        this.content = requestUpdateTask.getContent();
        this.start_date = requestUpdateTask.getStart_date().atStartOfDay();
        this.end_date = requestUpdateTask.getEnd_date().atStartOfDay();
    }
}