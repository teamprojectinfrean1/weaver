package com.task.weaver.domain.task.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.Issue;
import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.tasktag.TaskTag;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface TaskService {

    Task getTask(Long taskId)
            throws NotFoundException, AuthorizationException;
    Task getTask(Issue issue)
            throws NotFoundException, AuthorizationException;

    Page<Task> getTasks(Project project, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(StatusTag statusTag, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(User user, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(Project project, TaskManager taskManager, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(Project project, Long taskManagerId, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(Long projectId, TaskManager taskManager, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(Long projectId, Long taskManagerId, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(TaskTag taskTag, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(Long taskTagId, Pageable pageable)
            throws NotFoundException, AuthorizationException;

    Task addTask(Task task)
            throws AuthorizationException;
    Task addTask(Project project, User user, TaskTag taskTag)
            throws AuthorizationException;
    Task addTask(RequestCreateTask request)
            throws AuthorizationException;

    void deleteTask(Task task)
            throws NotFoundException, AuthorizationException;
    void deleteTask(Long taskId)
            throws NotFoundException, AuthorizationException;

    Task updateTask(Task originalTask, Task newTask)
            throws NotFoundException, AuthorizationException;
    Task updateTask(Long originalTaskId, Task newTask)
            throws NotFoundException, AuthorizationException;
}
