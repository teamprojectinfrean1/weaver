package com.task.weaver.domain.task.entity;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "TASK_AUTHORITY")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TaskAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_authority_id")
    private Long taskAuthorityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    @Column(name = "code", length = 10)
    private String code;

    public void updateTaskAuthority(TaskAuthority taskAuthority) {
        this.project = taskAuthority.getProject();
        this.user = taskAuthority.getUser();
        this.task = taskAuthority.getTask();
        this.code = taskAuthority.getCode();
    }

    public void updateTaskAuthority(RequestUpdateTaskAuthority taskAuthority) {
        this.project = taskAuthority.getProject();
        this.user = taskAuthority.getUser();
        this.task = taskAuthority.getTask();
        this.code = taskAuthority.getCode();
    }
}
