package com.task.weaver.domain.task.service.impl;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.exception.project.AuthorizationException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.task.TaskNotFoundException;
import com.task.weaver.domain.issue.dto.request.RequestIssueForTask;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.task.dto.request.RequestCreateTask;
import com.task.weaver.domain.task.dto.request.RequestUpdateTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTask;
import com.task.weaver.domain.task.dto.response.ResponseGetTaskList;
import com.task.weaver.domain.task.dto.response.ResponseUpdateDetail;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import com.task.weaver.domain.task.service.TaskService;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final MemberRepository memberRepository;

    @Override
    public ResponseGetTask getTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(taskId))));

        Member modifier = task.getModifier();
        UserOauthMember userOauthMember = modifier.resolveMemberByLoginType();

        List<RequestIssueForTask> requestIssueForTasks = task.getIssueList().stream()
                .map(issue -> new RequestIssueForTask(issue.getIssueId(), issue.getIssueTitle())).toList();

        ResponseGetTask responseTask = new ResponseGetTask(task, requestIssueForTasks);
        ResponseUpdateDetail responseUpdateDetail = ResponseUpdateDetail.builder()
                .memberUuid(modifier.getId())
                .userNickname(userOauthMember.getNickname())
                .updatedDate(task.getModDate())
                .build();

        responseTask.setLastUpdateDetail(responseUpdateDetail);

        return responseTask;
    }

    @Override
    public ResponsePageResult<ResponseGetTaskList, Task> getTasks(int page, int size, UUID projectId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(ErrorCode.PROJECT_NOT_FOUND,
                        ErrorCode.PROJECT_NOT_FOUND.getMessage()));

        Pageable pageable = getPageable(Sort.by("taskId").descending(), page, size);
        Page<Task> taskPage = taskRepository.findByProject(project, pageable);
        Function<Task, ResponseGetTaskList> fn = ResponseGetTaskList::new;
        return new ResponsePageResult<>(taskPage, fn);
    }

    private Pageable getPageable(Sort sort, int page, int size) {
        return PageRequest.of(page - 1, size, sort);
    }

    @Override
    public Page<Task> getTasks(String status, Pageable pageable) {
        return taskRepository.findByStatus(status, pageable);
    }

    @Override
    public UUID addTask(RequestCreateTask request) throws AuthorizationException {
        Member writer = memberRepository.findById(request.getWriterUuid())
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
    public void deleteTask(Task task) {
        taskRepository.delete(task);
    }

    @Override
    public void deleteTask(UUID taskId) {
        taskRepository.deleteById(taskId);
    }

    @Override
    public ResponseGetTask updateTask(Task originalTask, Task newTask) {
        Task task = taskRepository.findById(originalTask.getTaskId()).get();
        task.updateTask(newTask);
        task = taskRepository.save(task);
        List<RequestIssueForTask> requestIssueForTasks = task.getIssueList().stream()
                .map(issue -> new RequestIssueForTask(issue.getIssueId(), issue.getIssueTitle())).toList();
        return new ResponseGetTask(task, requestIssueForTasks);
    }

    @Override
    public ResponseGetTask updateTask(UUID originalTaskId, Task newTask) {
        Task task = taskRepository.findById(originalTaskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(originalTaskId))));

        task.updateTask(newTask);
        taskRepository.save(task);
        List<RequestIssueForTask> requestIssueForTasks = task.getIssueList().stream()
                .map(issue -> new RequestIssueForTask(issue.getIssueId(), issue.getIssueTitle())).toList();
        return new ResponseGetTask(task, requestIssueForTasks);
    }

    @Override
    public ResponseGetTask updateTask(UUID taskId, RequestUpdateTask requestUpdateTask) {
        Task findTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(new Throwable(String.valueOf(taskId))));

        Member updater = memberRepository.findById(requestUpdateTask.getUpdaterUuid())
                .orElseThrow(() -> new UserNotFoundException(
                        new Throwable(String.valueOf(requestUpdateTask.getUpdaterUuid()))));

        findTask.updateTask(requestUpdateTask, updater);
        taskRepository.save(findTask);
        List<RequestIssueForTask> requestIssueForTasks = findTask.getIssueList().stream()
                .map(issue -> new RequestIssueForTask(issue.getIssueId(), issue.getIssueTitle())).toList();
        return new ResponseGetTask(findTask, requestIssueForTasks);
    }

    @Override
    public ResponseGetTask updateTaskStatus(UUID taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(ErrorCode.TASK_NOT_FOUND, ErrorCode.TASK_NOT_FOUND.getMessage()));
        task.setStatus(status);
        taskRepository.save(task);
        List<RequestIssueForTask> requestIssueForTasks = task.getIssueList().stream()
                .map(issue -> new RequestIssueForTask(issue.getIssueId(), issue.getIssueTitle())).toList();
        return new ResponseGetTask(task, requestIssueForTasks);
    }
}