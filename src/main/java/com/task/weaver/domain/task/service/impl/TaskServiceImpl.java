package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.task.TaskNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestGetTaskPage;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import com.task.weaver.domain.task.service.TaskService;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseGetTask getTask(Long taskId) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(taskId))));

        ResponseGetTask responseTask = new ResponseGetTask(task);
        return responseTask;
    }

    @Override
    public ResponseTask getTask(Issue issue) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public ResponsePageResult<ResponseGetTaskList, Task> getTasks(RequestGetTaskPage requestGetTaskPage) throws NotFoundException, AuthorizationException {
//        Page<Task> tasks = taskRepository.findByProject(project, pageable);
        Project project = projectRepository.findById(requestGetTaskPage.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(requestGetTaskPage.getProjectId()))));

        Pageable pageable = requestGetTaskPage.getPageable(Sort.by("taskId").descending());
        Page<Task> taskPage = taskRepository.findByProject(project, pageable);
        Function<Task, ResponseGetTaskList> fn = Task -> (new ResponseGetTaskList(Task));
        return new ResponsePageResult<>(taskPage, fn);
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
    public ResponseGetTask addTask(RequestCreateTask request) throws AuthorizationException {
        User user = userRepository.findById(request.getWriterUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(request.getProjectId()))));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(request.getProjectId()))));

        Task entity = request.toEntity(user, project);
        Task save = taskRepository.save(entity);

        ResponseGetTask responseTask = new ResponseGetTask(save);
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
    public ResponseGetTask updateTask(Task originalTask, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTask.getTaskId()).get();
        task.updateTask(newTask);
        return new ResponseGetTask(task);
    }

    @Override
    public ResponseGetTask updateTask(Long originalTaskId, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTaskId).get();
        task.updateTask(newTask);
        return new ResponseGetTask(task);
    }


    @Override
    public ResponseGetTask updateTask(Long taskId, RequestUpdateTask requestUpdateUser) throws NotFoundException, AuthorizationException {
        Task findTask = taskRepository.findById(taskId).get();
        findTask.updateTask(requestUpdateUser);

        ResponseGetTask responseTask = new ResponseGetTask(findTask);
        return responseTask;
    }
}