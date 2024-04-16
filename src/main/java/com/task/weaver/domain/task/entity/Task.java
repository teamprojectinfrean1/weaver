package com.task.weaver.domain.task.entity;

import com.task.weaver.domain.BaseEntity;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.member.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(columnDefinition = "BINARY(16)", name = "task_id")
    private UUID taskId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_member_id")
    private User user;

    @OneToMany(mappedBy = "task")
    @Builder.Default
    private List<Issue> issueList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "last_update_member_id")
    private User modifier;

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

    public String getTitle() {
        return this.taskTitle;
    }

    public void updateTask(Task newTask) {
        this.project = newTask.getProject();
        this.user = newTask.getUser();
        this.taskTitle = newTask.getTitle();
        this.taskContent = newTask.getTaskContent();
        this.startDate = newTask.getStartDate();
        this.endDate = newTask.getEndDate();
    }

    public void updateTask(RequestUpdateTask requestUpdateTask, User updater) {
        this.taskTitle = requestUpdateTask.getTaskTitle();
        this.taskContent = requestUpdateTask.getTaskContent();
        this.startDate = requestUpdateTask.getStartDate().atStartOfDay();
        this.endDate = requestUpdateTask.getEndDate().atStartOfDay();
        this.modifier = updater;
        this.editDeletePermission = requestUpdateTask.getEditDeletePermission();
    }
}