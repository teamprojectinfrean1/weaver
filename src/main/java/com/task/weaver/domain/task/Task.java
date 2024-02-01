package com.task.weaver.domain.task;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
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

    @Column(name = "taskname", length = 100)
    private String taskName;

    @Column(name = "detail")
    private String detail;

    @Column(name = "due_date")
    private LocalDateTime dueDate;
}


