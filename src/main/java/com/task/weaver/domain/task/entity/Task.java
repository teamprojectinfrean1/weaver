package com.task.weaver.domain.task.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.task.weaver.domain.BaseEntity;
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
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
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
    @OneToOne
    @JoinColumn(name = "last_update_user_id")
    private User modifier;

    @Column(name = "taskTitle", length = 100)
    private String taskTitle;
    @Column(name = "taskContent")
    private String taskContent;
    @Column(name = "startDate")
    private LocalDateTime startDate;
    @Column(name = "endDate")
    private LocalDateTime endDate;
    private String editDeletePermission;

    public String getTitle() {
        return this.taskTitle;
    }

    public Task(Long taskId, Project project, StatusTag statusTag, User user, String taskName, String detail, LocalDateTime startDate, LocalDateTime endDate) {
        this.taskId = taskId;
        this.project = project;
        this.statusTag = statusTag;
        this.user = user;
        this.taskTitle = taskName;
        this.taskContent = detail;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void updateTask(Task newTask) {
        this.project = newTask.getProject();
        this.statusTag = newTask.getStatusTag();
        this.user = newTask.getUser();
        this.taskTitle = newTask.getTitle();
        this.taskContent = newTask.getTaskContent();
        this.startDate = newTask.getStartDate();
        this.endDate = newTask.getEndDate();
    }

    public void updateTask(RequestUpdateTask requestUpdateTask) {
//        this.project = requestUpdateTask.getProject();
//        this.statusTag = requestUpdateTask.getStatusTag();
//        this.user = requestUpdateTask.getUser();
        this.taskTitle = requestUpdateTask.getTaskTitle();
        this.taskContent = requestUpdateTask.getTaskContent();
        this.startDate = requestUpdateTask.getStartDate().atStartOfDay();
        this.endDate = requestUpdateTask.getEndDate().atStartOfDay();
        this.editDeletePermission = requestUpdateTask.getEditDeletePermission();
    }
}