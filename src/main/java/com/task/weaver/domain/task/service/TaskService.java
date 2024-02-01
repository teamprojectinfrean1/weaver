package com.task.weaver.domain.task.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.tasktag.TaskTag;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface TaskService {

    ResponseTask getTask(Long taskId)
            throws NotFoundException, AuthorizationException;
    ResponseTask getTask(Issue issue)
            throws NotFoundException, AuthorizationException;

    Page<Task> getTasks(Project project, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(StatusTag statusTag, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(User user, Pageable pageable)
            throws NotFoundException, AuthorizationException;

    ResponseTask addTask(RequestCreateTask request)
            throws AuthorizationException;

    ResponseTask addTask(Task task)
            throws AuthorizationException;

    void deleteTask(Task task)
            throws NotFoundException, AuthorizationException;
    void deleteTask(Long taskId)
            throws NotFoundException, AuthorizationException;

    ResponseTask updateTask(Task originalTask, Task newTask)
            throws NotFoundException, AuthorizationException;
    ResponseTask updateTask(Long originalTaskId, Task newTask)
            throws NotFoundException, AuthorizationException;
    ResponseTask updateTask(Long taskId, RequestUpdateTask requestUpdateUser)
            throws NotFoundException, AuthorizationException;
}