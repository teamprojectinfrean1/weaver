package com.task.weaver.domain.task.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.issue.Issue;
import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.status.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestCreateTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.tasktag.TaskTag;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;

public interface TaskAuthorityService {

    TaskAuthority getTaskAuthority(Long taskAuthorityId)
            throws NotFoundException, AuthorizationException;
    TaskAuthority getTaskAuthority(Project project, Task task, TaskManager taskManager)
            throws NotFoundException, AuthorizationException;

    Page<TaskAuthority> getTaskAuthorities(Task task, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<TaskAuthority> getTaskAuthorities(User user, Pageable pageable)
            throws NotFoundException, AuthorizationException;

    TaskAuthority addTaskAuthority(Task task, TaskManager taskManager, String code)
            throws AuthorizationException;
    TaskAuthority addTaskAuthority(RequestCreateTaskAuthority request)
            throws AuthorizationException;

    // 권한이 삭제되면 Task에서 추방
    void deleteTaskAuthority(TaskAuthority taskAuthority)
            throws NotFoundException, AuthorizationException;
    void deleteTaskAuthority(Long taskAuthorityId)
            throws NotFoundException, AuthorizationException;

    TaskAuthority updateTaskAuthority(TaskAuthority originalTaskAuthority, TaskAuthority newTaskAuthority)
            throws NotFoundException, AuthorizationException;
    TaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, TaskAuthority newTaskAuthority)
            throws NotFoundException, AuthorizationException;

}
