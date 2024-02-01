package com.task.weaver.domain.taskmember.entity;

import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.dto.request.RequestUpdateTaskManager;
import com.task.weaver.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "TASK_MANAGER")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TaskManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_manager_id")
    private Long taskMangerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    private Task task;

    public void updateTaskManager(TaskManager taskManager) {
        this.user = taskManager.getUser();
        this.task = taskManager.getTask();
    }

    public void updateTaskManager(RequestUpdateTaskManager taskManager) {
        this.user = taskManager.getUser();
        this.task = taskManager.getTask();
    }

}
