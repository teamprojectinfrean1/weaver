package com.task.weaver.domain.task.entity;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_tag_id")
    private StatusTag statusTag;

    @ManyToOne
    @JoinColumn(name = "creator_user_id")
    private User user;

    @Column(name = "taskName", length = 100)
    private String taskName;

    @Column(name = "detail")
    private String detail;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    public String getTaskName() {
        return this.taskName;
    }

    public Task(Long taskId, Project project, StatusTag statusTag, User user, String taskName, String detail, LocalDateTime dueDate) {
        this.taskId = taskId;
        this.project = project;
        this.statusTag = statusTag;
        this.user = user;
        this.taskName = taskName;
        this.detail = detail;
        this.dueDate = dueDate;
    }

    public void updateTask(Task newTask) {
        this.project = newTask.getProject();
        this.statusTag = newTask.getStatusTag();
        this.user = newTask.getUser();
        this.taskName = newTask.getTaskName();
        this.detail = newTask.getDetail();
        this.dueDate = newTask.getDueDate();
    }

    public void updateTask(RequestUpdateTask requestUpdateTask) {
        this.project = requestUpdateTask.getProject();
        this.statusTag = requestUpdateTask.getStatusTag();
        this.user = requestUpdateTask.getUser();
        this.taskName = requestUpdateTask.getTaskName();
        this.detail = requestUpdateTask.getDetail();
        this.dueDate = requestUpdateTask.getDueDate().atStartOfDay();
    }
}