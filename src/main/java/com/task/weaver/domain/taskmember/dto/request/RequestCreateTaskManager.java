package com.task.weaver.domain.taskmember.dto.request;

import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestCreateTaskManager {
    private User user;
    private Task task;

    public TaskManager toEntity() {
        return TaskManager.builder()
                .user(user)
                .task(task)
                .build();
    }
}
