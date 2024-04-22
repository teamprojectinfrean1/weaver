package com.task.weaver.domain.task.service.impl;

import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.task.dto.request.RequestCreateTaskAuthority;
import com.task.weaver.domain.task.dto.request.RequestUpdateTaskAuthority;
import com.task.weaver.domain.task.dto.response.ResponseTaskAuthority;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.task.repository.TaskAuthorityRepository;
import com.task.weaver.domain.task.service.TaskAuthorityService;
import java.awt.print.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskAuthorityServiceImpl implements TaskAuthorityService {

    private final TaskAuthorityRepository taskAuthorityRepository;

    @Override
    public ResponseTaskAuthority getTaskAuthority(Long taskAuthorityId) {

        TaskAuthority taskAuthority = taskAuthorityRepository.findById(taskAuthorityId).get();
        ResponseTaskAuthority responseTaskAuthority = new ResponseTaskAuthority(taskAuthority);

        return responseTaskAuthority;
    }

    @Override
    public Page<TaskAuthority> getTaskAuthorities(Task task, Pageable pageable) {
        // return (Page<TaskAuthority>) taskAuthorityRepository.findById(task.getTaskId()).orElseThrow(
        //         () -> new NotFoundException());
        return null;
    }

    @Override
    public Page<TaskAuthority> getTaskAuthorities(User user, Pageable pageable) {
        // return (Page<TaskAuthority>) taskAuthorityRepository.findById(user.getUserId()).orElseThrow(
        //         () -> new NotFoundException());
        return null;
    }

    @Override
    public TaskAuthority addTaskAuthority(TaskAuthority taskAuthority) {
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
    public void deleteTaskAuthority(TaskAuthority taskAuthority) {
        taskAuthorityRepository.delete(taskAuthority);
    }

    @Override
    public void deleteTaskAuthority(Long taskAuthorityId) {
        taskAuthorityRepository.deleteById(taskAuthorityId);
    }

    @Override
    public TaskAuthority updateTaskAuthority(TaskAuthority originalTaskAuthority, TaskAuthority newTaskAuthority) {
        TaskAuthority taskAuthority = taskAuthorityRepository.findById(originalTaskAuthority.getTaskAuthorityId())
                .get();
        taskAuthority.updateTaskAuthority(taskAuthority);
        return taskAuthority;
    }

    @Override
    public TaskAuthority updateTaskAuthority(Long originalTaskAuthorityId, TaskAuthority newTaskAuthority) {
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