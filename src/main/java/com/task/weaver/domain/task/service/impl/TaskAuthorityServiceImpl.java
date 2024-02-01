package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.dto.request.RequestCreateTaskAuthority;
import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.dto.response.ResponseTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.task.repository.TaskAuthorityRepository;
import com.task.weaver.domain.task.service.TaskAuthorityService;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import com.task.weaver.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;

@Service
@RequiredArgsConstructor
public class TaskAuthorityServiceImpl implements TaskAuthorityService {

    private final TaskAuthorityRepository taskAuthorityRepository;

    @Override
    public ResponseTaskAuthority getTaskAuthority(Long taskAuthorityId) throws NotFoundException, AuthorizationException {

        TaskAuthority taskAuthority = taskAuthorityRepository.findById(taskAuthorityId).get();
        ResponseTaskAuthority responseTaskAuthority = new ResponseTaskAuthority(taskAuthority);

        return responseTaskAuthority;
    }

    @Override
    public ResponseTaskAuthority getTaskAuthority(Project project, Task task, TaskManager taskManager) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<TaskAuthority> getTaskAuthorities(Task task, Pageable pageable) throws NotFoundException, AuthorizationException {
        return (Page<TaskAuthority>) taskAuthorityRepository.findById(task.getTaskId()).orElseThrow(
                () -> new NotFoundException());
    }

    @Override
    public Page<TaskAuthority> getTaskAuthorities(User user, Pageable pageable) throws NotFoundException, AuthorizationException {
        return (Page<TaskAuthority>) taskAuthorityRepository.findById(user.getUserId()).orElseThrow(
                () -> new NotFoundException());
    }

    @Override
    public TaskAuthority addTaskAuthority(TaskAuthority taskAuthority) throws AuthorizationException {
        taskAuthorityRepository.save(taskAuthority);
        return taskAuthority;
    }

    @Override
    public ResponseTaskAuthority addTaskAuthority(RequestCreateTaskAuthority requestCreateTaskAuthority) {
        TaskAuthority entity = requestCreateTaskAuthority.toEntity();
        TaskAuthority save = taskAuthorityRepository.save(entity);

        ResponseTaskAuthority responseTaskAuthority = new ResponseTaskAuthority(save);
        return responseTaskAuthority;
    }

    @Override
    public void deleteTaskAuthority(TaskAuthority taskAuthority) throws NotFoundException, AuthorizationException {
        taskAuthorityRepository.delete(taskAuthority);
    }

    @Override
    public void deleteTaskAuthority(Long taskAuthorityId) throws NotFoundException, AuthorizationException {
        taskAuthorityRepository.deleteById(taskAuthorityId);
    }

    @Override
    public TaskAuthority updateTaskAuthority(TaskAuthority originalTaskAuthority, TaskAuthority newTaskAuthority) throws NotFoundException, AuthorizationException {
        TaskAuthority taskAuthority = taskAuthorityRepository.findById(originalTaskAuthority.getTaskAuthorityId()).get();
        taskAuthority.updateTaskAuthority(taskAuthority);
        return taskAuthority;
    }

    @Override
    public TaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, TaskAuthority newTaskAuthority) throws NotFoundException, AuthorizationException {
        TaskAuthority taskAuthority = taskAuthorityRepository.findById(originalTaskAuthorityId).get();
        taskAuthority.updateTaskAuthority(taskAuthority);
        return taskAuthority;
    }

    @Override
    public ResponseTaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, RequestUpdateTaskAuthority requestUpdateTaskAuthority) {
        TaskAuthority taskAuthority = taskAuthorityRepository.findById(originalTaskAuthorityId).get();
        taskAuthority.updateTaskAuthority(taskAuthority);

        ResponseTaskAuthority responseTaskAuthority = new ResponseTaskAuthority(taskAuthority);
        return responseTaskAuthority;
    }


}