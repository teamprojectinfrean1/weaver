package com.task.weaver.domain.task.service;

import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.entity.Task;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TaskService {

    ResponseGetTask getTask(UUID taskId);

    ResponsePageResult<ResponseGetTaskList, Task> getTasks(int page, int size, UUID projectId);

    Page<Task> getTasks(String status, Pageable pageable);

    UUID addTask(RequestCreateTask request);

    void deleteTask(Task task);

    void deleteTask(UUID taskId);

    ResponseGetTask updateTask(Task originalTask, Task newTask);

    ResponseGetTask updateTask(UUID originalTaskId, Task newTask);

    ResponseGetTask updateTask(UUID taskId, RequestUpdateTask requestUpdateUser);

    ResponseGetTask updateTaskStatus(UUID taskId, String status);
}