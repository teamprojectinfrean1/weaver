package com.task.weaver.domain.task.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;

import com.task.weaver.domain.project.entity.Project;

import com.task.weaver.domain.task.dto.request.RequestCreateTaskAuthority;
import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.dto.response.ResponseTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface TaskAuthorityService {

    ResponseTaskAuthority getTaskAuthority(Long taskAuthorityId)
            throws NotFoundException, AuthorizationException;

    Page<TaskAuthority> getTaskAuthorities(Task task, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<TaskAuthority> getTaskAuthorities(User user, Pageable pageable)
            throws NotFoundException, AuthorizationException;

    TaskAuthority addTaskAuthority(TaskAuthority taskAuthority)
            throws AuthorizationException;

    ResponseTaskAuthority addTaskAuthority(RequestCreateTaskAuthority requestCreateTaskAuthority);

    // 권한이 삭제되면 Task에서 추방
    void deleteTaskAuthority(TaskAuthority taskAuthority)
            throws NotFoundException, AuthorizationException;
    void deleteTaskAuthority(Long taskAuthorityId)
            throws NotFoundException, AuthorizationException;

    TaskAuthority updateTaskAuthority(TaskAuthority originalTaskAuthority, TaskAuthority newTaskAuthority)
            throws NotFoundException, AuthorizationException;
    TaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, TaskAuthority newTaskAuthority)
            throws NotFoundException, AuthorizationException;

    ResponseTaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, RequestUpdateTaskAuthority requestUpdateTaskAuthority);
}