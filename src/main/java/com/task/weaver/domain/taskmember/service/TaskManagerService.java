package com.task.weaver.domain.taskmember.service;

import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.dto.request.RequestCreateTaskManager;
import com.task.weaver.domain.taskmember.dto.request.RequestUpdateTaskManager;
import com.task.weaver.domain.taskmember.dto.response.ResponseTaskManager;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskManagerService {

    ResponseTaskManager getTaskManager(Long taskManagerId);
    Page<TaskManager> getTaskManagers(Task task, Pageable pageable);

    TaskManager addTaskManager(TaskManager taskManager);
    ResponseTaskManager addTaskManager(RequestCreateTaskManager request);

    void deleteTaskManager(TaskManager taskManager);
    void deleteTaskManager(Long taskManagerId);

    ResponseTaskManager updateTaskManager(TaskManager originalTask, TaskManager newTaskManager);
    ResponseTaskManager updateTaskManager(Long originalTaskId, TaskManager newTaskManager);
    ResponseTaskManager updateTaskManager(Long originalTaskId, RequestUpdateTaskManager requestUpdateTaskManager);
}
