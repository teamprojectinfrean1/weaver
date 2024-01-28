package com.task.weaver.domain.taskmember.service.impl;

import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.dto.request.RequestCreateTaskManager;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.taskmember.service.TaskManagerService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class TaskManagerServiceImpl implements TaskManagerService {
    @Override
    public TaskManager getTaskManager(Long taskManagerId) {
        return null;
    }

    @Override
    public Page<TaskManager> getTaskManagers(Task task, Pageable pageable) {
        return null;
    }

    @Override
    public TaskManager addTaskManager(Task task) {
        return null;
    }

    @Override
    public TaskManager addTaskManager(Long taskId) {
        return null;
    }

    @Override
    public TaskManager addTaskManager(RequestCreateTaskManager request) {
        return null;
    }

    @Override
    public void deleteTaskManager(TaskManager taskManager) {

    }

    @Override
    public void deleteTaskManager(Long taskManagerId) {

    }

    @Override
    public TaskManager updateTaskManager(TaskManager originalTask, TaskManager newTaskManager) {
        return null;
    }

    @Override
    public TaskManager updateTaskManager(Long originalTaskId, TaskManager newTaskManager) {
        return null;
    }
}
