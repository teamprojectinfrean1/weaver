package com.task.weaver.domain.task.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.status.entity.Status;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestGetTaskPage;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskService {

    ResponseGetTask getTask(Long taskId)
            throws NotFoundException, AuthorizationException;
    ResponseTask getTask(Issue issue)
            throws NotFoundException, AuthorizationException;

    ResponsePageResult<ResponseGetTaskList, Task> getTasks(RequestGetTaskPage requestGetTaskPage)
            throws NotFoundException, AuthorizationException;
    Page<Task> getTasks(Status statusTag, Pageable pageable)
            throws NotFoundException, AuthorizationException;
//    Page<Task> getTasks(User user, Pageable pageable)
//            throws NotFoundException, AuthorizationException;

    ResponseGetTask addTask(RequestCreateTask request)
            throws AuthorizationException;

    ResponseTask addTask(Task task)
            throws AuthorizationException;

    void deleteTask(Task task)
            throws NotFoundException, AuthorizationException;
    void deleteTask(Long taskId)
            throws NotFoundException, AuthorizationException;

    ResponseGetTask updateTask(Task originalTask, Task newTask)
            throws NotFoundException, AuthorizationException;
    ResponseGetTask updateTask(Long originalTaskId, Task newTask)
            throws NotFoundException, AuthorizationException;
    ResponseGetTask updateTask(Long taskId, RequestUpdateTask requestUpdateUser)
            throws NotFoundException, AuthorizationException;
}