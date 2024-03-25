package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.task.TaskNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.issue.dto.request.RequestIssueForTask;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.status.entity.Status;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestGetTaskPage;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.dto.response.ResponseTask;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import com.task.weaver.domain.task.service.TaskService;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public ResponseGetTask getTask(UUID taskId) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(taskId))));

        User modifier = task.getModifier();

        ResponseGetTask responseTask = new ResponseGetTask(task);

        ResponseUpdateDetail responseUpdateDetail = ResponseUpdateDetail.builder()
                .userUuid(modifier.getUserId())
                .userNickname(modifier.getNickname())
                .updatedDate(task.getModDate())
                .build();

        responseTask.setLastUpdateDetail(responseUpdateDetail);

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
    public Page<Task> getTasks(Status statusTag, Pageable pageable) throws NotFoundException, AuthorizationException {
        Page<Task> tasks = taskRepository.findByStatusTag(statusTag, pageable);
        return tasks;
    }

//    @Override
//    public Page<Task> getTasks(User user, Pageable pageable) throws NotFoundException, AuthorizationException {
//        Page<Task> tasks = taskRepository.findByUser(user, pageable);
//        return tasks;
//    }


    @Override
    public UUID addTask(RequestCreateTask request) throws AuthorizationException {
        User writer = userRepository.findById(request.getWriterUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(request.getProjectId()))));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(request.getProjectId()))));

        log.info("writer id : " + request.getWriterUuid());
        log.info("project id : " + request.getProjectId());

        Task entity = request.toEntity(writer, project);

        Task save = taskRepository.save(entity);

        return save.getTaskId();
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
    public void deleteTask(UUID taskId) throws NotFoundException, AuthorizationException {
        taskRepository.deleteById(taskId);
    }

    @Override
    public ResponseGetTask updateTask(Task originalTask, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTask.getTaskId()).get();
        task.updateTask(newTask);
        return new ResponseGetTask(task);
    }

    @Override
    public ResponseGetTask updateTask(UUID originalTaskId, Task newTask) throws NotFoundException, AuthorizationException {
        Task task = taskRepository.findById(originalTaskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(originalTaskId))));

        task.updateTask(newTask);
        return new ResponseGetTask(task);
    }


    @Override
    public ResponseGetTask updateTask(UUID taskId, RequestUpdateTask requestUpdateTask) throws NotFoundException, AuthorizationException {
        Task findTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(taskId))));

        User updater = userRepository.findById(requestUpdateTask.getUpdaterUuid())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(requestUpdateTask.getUpdaterUuid()))));

        List<RequestIssueForTask> requestIssueForTasks = requestUpdateTask.getIssueList();
        List<Issue> issueList = new ArrayList<>();

        findTask.updateTask(requestUpdateTask, updater);

        ResponseGetTask responseTask = new ResponseGetTask(findTask);
        return responseTask;
    }
}