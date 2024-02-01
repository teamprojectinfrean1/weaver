package com.task.weaver.domain.taskmember.service;

import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.dto.request.RequestCreateTaskManager;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface TaskManagerService {

    TaskManager getTaskManager(Long taskManagerId);
    Page<TaskManager> getTaskManagers(Task task, Pageable pageable);

    TaskManager addTaskManager(Task task);
    TaskManager addTaskManager(Long taskId);
    TaskManager addTaskManager(RequestCreateTaskManager request);

    void deleteTaskManager(TaskManager taskManager);
    void deleteTaskManager(Long taskManagerId);

    TaskManager updateTaskManager(TaskManager originalTask, TaskManager newTaskManager);
    TaskManager updateTaskManager(Long originalTaskId, TaskManager newTaskManager);
}
