package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.request.RequestCreateTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.task.service.TaskAuthorityService;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
public class TaskAuthorityServiceImpl implements TaskAuthorityService {

    @Override
    public TaskAuthority getTaskAuthority(Long taskAuthorityId) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public TaskAuthority getTaskAuthority(Project project, Task task, TaskManager taskManager) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<TaskAuthority> getTaskAuthorities(Task task, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<TaskAuthority> getTaskAuthorities(User user, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public TaskAuthority addTaskAuthority(Task task, TaskManager taskManager, String code) throws AuthorizationException {
        return null;
    }

    @Override
    public TaskAuthority addTaskAuthority(RequestCreateTaskAuthority request) throws AuthorizationException {
        return null;
    }

    @Override
    public void deleteTaskAuthority(TaskAuthority taskAuthority) throws NotFoundException, AuthorizationException {

    }

    @Override
    public void deleteTaskAuthority(Long taskAuthorityId) throws NotFoundException, AuthorizationException {

    }

    @Override
    public TaskAuthority updateTaskAuthority(TaskAuthority originalTaskAuthority, TaskAuthority newTaskAuthority) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public TaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, TaskAuthority newTaskAuthority) throws NotFoundException, AuthorizationException {
        return null;
    }
}
