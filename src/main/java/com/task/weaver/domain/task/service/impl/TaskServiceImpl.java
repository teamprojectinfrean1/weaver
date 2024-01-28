package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.service.TaskService;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.tasktag.TaskTag;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public Task getTask(Long taskId) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Task getTask(Issue issue) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(Project project, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(StatusTag statusTag, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(User user, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(Project project, TaskManager taskManager, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(Project project, Long taskManagerId, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(Long projectId, TaskManager taskManager, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(Long projectId, Long taskManagerId, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(TaskTag taskTag, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(Long taskTagId, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Task addTask(Task task) throws AuthorizationException {
        return null;
    }

    @Override
    public Task addTask(Project project, User user, TaskTag taskTag) throws AuthorizationException {
        return null;
    }

    @Override
    public Task addTask(RequestCreateTask request) throws AuthorizationException {
        return null;
    }

    @Override
    public void deleteTask(Task task) throws NotFoundException, AuthorizationException {

    }

    @Override
    public void deleteTask(Long taskId) throws NotFoundException, AuthorizationException {

    }

    @Override
    public Task updateTask(Task originalTask, Task newTask) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Task updateTask(Long originalTaskId, Task newTask) throws NotFoundException, AuthorizationException {
        return null;
    }
}
