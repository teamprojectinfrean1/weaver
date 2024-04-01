package com.task.weaver.domain.task.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;


import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestGetTaskPage;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;


public interface TaskService {

    ResponseGetTask getTask(UUID taskId)
            throws NotFoundException, AuthorizationException;

    ResponsePageResult<ResponseGetTaskList, Task> getTasks(RequestGetTaskPage requestGetTaskPage)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(String status, Pageable pageable)
            throws NotFoundException, AuthorizationException;
//    Page<Task> getTasks(User user, Pageable pageable)
//            throws NotFoundException, AuthorizationException;

    UUID addTask(RequestCreateTask request)
            throws AuthorizationException;

    void deleteTask(Task task)
            throws NotFoundException, AuthorizationException;
    void deleteTask(UUID taskId)
            throws NotFoundException, AuthorizationException;

    ResponseGetTask updateTask(Task originalTask, Task newTask)
            throws NotFoundException, AuthorizationException;
    ResponseGetTask updateTask(UUID originalTaskId, Task newTask)
            throws NotFoundException, AuthorizationException;
    ResponseGetTask updateTask(UUID taskId, RequestUpdateTask requestUpdateUser)
            throws NotFoundException, AuthorizationException;

    ResponseGetTask updateTaskStatus(UUID taskId, String status);
}