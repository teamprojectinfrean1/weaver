package com.task.weaver.domain.taskmember.dto.response;

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
public class ResponseTaskManager {

    private Long taskManagerId;
    private User user;
    private Task task;

    public ResponseTaskManager(TaskManager taskManager) {
        this.taskManagerId = taskManager.getTaskMangerId();
        this.user = taskManager.getUser();
        this.task = taskManager.getTask();
    }
}
