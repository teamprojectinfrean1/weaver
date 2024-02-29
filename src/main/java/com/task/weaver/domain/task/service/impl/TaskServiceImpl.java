package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import com.task.weaver.domain.task.service.TaskService;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.swing.*;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseTask getTask(Long taskId) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(taskId).get();
        ResponseTask responseTask = new ResponseTask(task);
        return responseTask;
    }

    @Override
    public ResponseTask getTask(Issue issue) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Task> getTasks(Project project, Pageable pageable) throws NotFoundException, AuthorizationException {
        Page<Task> tasks = taskRepository.findByProject(project, pageable);
        return tasks;
    }

    @Override
    public Page<Task> getTasks(StatusTag statusTag, Pageable pageable) throws NotFoundException, AuthorizationException {
        Page<Task> tasks = taskRepository.findByStatusTag(statusTag, pageable);
        return tasks;
    }

//    @Override
//    public Page<Task> getTasks(User user, Pageable pageable) throws NotFoundException, AuthorizationException {
//        Page<Task> tasks = taskRepository.findByUser(user, pageable);
//        return tasks;
//    }


    @Override
    public ResponseTask addTask(RequestCreateTask request) throws AuthorizationException {
        User user = userRepository.findById(request.getProject_id())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(request.getProject_id()))));

        Project project = projectRepository.findById(request.getProject_id())
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(request.getProject_id()))));

        Task entity = request.toEntity(user, project);
        Task save = taskRepository.save(entity);

        ResponseTask responseTask = new ResponseTask(save);
        return responseTask;
    }

    @Override
    public ResponseTask addTask(Task task) throws AuthorizationException {
        Task save = taskRepository.save(task);
        ResponseTask responseTask = new ResponseTask(save);
        return responseTask;
    }

    @Override
    public void deleteTask(Task task) throws NotFoundException, AuthorizationException {
        taskRepository.delete(task);
    }

    @Override
    public void deleteTask(Long taskId) throws NotFoundException, AuthorizationException {
        taskRepository.deleteById(taskId);
    }

    @Override
    public ResponseTask updateTask(Task originalTask, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTask.getTaskId()).get();
        task.updateTask(newTask);
        return new ResponseTask(task);
    }

    @Override
    public ResponseTask updateTask(Long originalTaskId, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTaskId).get();
        task.updateTask(newTask);
        return new ResponseTask(task);
    }


    @Override
    public ResponseTask updateTask(Long taskId, RequestUpdateTask requestUpdateUser) throws NotFoundException, AuthorizationException {
        Task findTask = taskRepository.findById(taskId).get();
        findTask.updateTask(requestUpdateUser);

        ResponseTask responseTask = new ResponseTask(findTask);
        return responseTask;
    }
}