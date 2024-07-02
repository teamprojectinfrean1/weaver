package com.task.weaver.domain.issue.service.impl;

import static com.task.weaver.common.exception.ErrorCode.ISSUE_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.PROJECT_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.TASK_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.issue.IssueNotFoundException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.task.TaskNotFoundException;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueStatusRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.dto.response.UpdateIssueStatus;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IssueServiceImpl implements IssueService {
	private static final String PROPERTIES = "modDate";

	private final IssueRepository issueRepository;
	private final MemberRepository memberRepository;
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;

	@Override
	public IssueResponse fetchIssue(UUID issueId) {
		return new IssueResponse(getIssue(issueId));
	}

	@Override
	public ResponsePageResult<GetIssueListResponse, Issue> fetchIssues(Status status,
																	   GetIssuePageRequest getIssuePageRequest) {
		Pageable pageable = getIssuePageRequest.getPageable(Sort.by(PROPERTIES).descending());
		Page<Issue> issuePage = projectRepository.findIssuePageByProjectId(getIssuePageRequest.projectId(),	String.valueOf(status), pageable);

		Function<Issue, GetIssueListResponse> fn = GetIssueListResponse::of;
		return new ResponsePageResult<>(issuePage, fn);
	}

	@Override
	public ResponsePageResult<GetIssueListResponse, Issue> fetchSearchIssues(String status, String filter, String word,
																			 GetIssuePageRequest getIssuePageRequest) {
		log.info("status = {}, filter = {}, word = {}", status, filter, word);
		Pageable pageable = getIssuePageRequest.getPageable(Sort.by(PROPERTIES).descending());
		Page<Issue> issuePage = issueRepository.findBySearch(getIssuePageRequest.projectId(), status, filter, word,
				pageable);
		Function<Issue, GetIssueListResponse> fn = GetIssueListResponse::of;
		return new ResponsePageResult<>(issuePage, fn);
	}

	@Override
	@Transactional
	public IssueResponse addIssue(CreateIssueRequest createIssueRequest) {
		Task task = getTask(createIssueRequest.taskId());
		Member creator = getMember(createIssueRequest.creatorId());
		Member assignee = hasAssigneeMember(createIssueRequest.assigneeId());

		Issue issue = CreateIssueRequest.dtoToEntity(task, creator, assignee, createIssueRequest);
		issueRepository.save(issue);

		task.addIssue(issue);
		taskRepository.save(task);
		return new IssueResponse(issue);
	}

	@Override
	@Transactional
	public IssueResponse updateIssue(UUID issueId, UpdateIssueRequest updateIssueRequest) {
		Issue issue = getIssue(issueId);
		Task task = getTask(updateIssueRequest.taskId());
		Member modifier = getMember(updateIssueRequest.modifierId());
		Member assignee = hasAssigneeMember(updateIssueRequest.assigneeId());

		issue.updateIssue(updateIssueRequest, task, modifier, assignee);
		issue.updateStatus(Status.fromName(updateIssueRequest.status()));

		return new IssueResponse(issue);
	}

	private Member hasAssigneeMember(Optional<UUID> assigneeId) {
		return assigneeId.map(this::getMember).orElse(null);
	}

	@Override
	@Transactional
	public UpdateIssueStatus updateIssueStatus(UUID issueId, UpdateIssueStatusRequest updateIssueStatusRequest) {
		Issue issue = getIssue(issueId);
		issue.updateStatus(Status.fromName(updateIssueStatusRequest.status()));
		issue.updateModifier(getMember(updateIssueStatusRequest.memberId()));
		return UpdateIssueStatus.of(issueId, issue.getStatus());
	}

	@Override
	@Transactional
	public UUID deleteIssue(UUID issueId) {
		issueRepository.deleteById(issueId);
		return issueId;
	}

	private Member getMember(final UUID memberId) {
		return memberRepository.findById(memberId)
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
	}

	private Issue getIssue(final UUID issueId) {
		return issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(ISSUE_NOT_FOUND, ISSUE_NOT_FOUND.getMessage()));
	}

	private Task getTask(final UUID taskId) {
		return taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND, TASK_NOT_FOUND.getMessage()));
	}

	private Project getProject(final UUID projectId) {
		return projectRepository.findById(projectId)
				.orElseThrow(() -> new ProjectNotFoundException(PROJECT_NOT_FOUND, PROJECT_NOT_FOUND.getMessage()));
	}

	private List<Issue> findIssuesByStatusAsync(final String status,
												final GetIssuePageRequest getIssuePageRequest) {

		Stream<CompletableFuture<List<Issue>>> completableFutureStream = findIssueStream(
				getIssuePageRequest.projectId(), status);
		return completableFutureStream
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	/**
	 * TODO: 2024-04-29, 월, 1:27  -JEON
	 *  TASK: findById with EntityGraph & Async version
	 */
	private Stream<CompletableFuture<List<Issue>>> findIssueStream(final UUID projectId,
																   String status) {
		return projectRepository.findById(projectId).stream()
				.map(project -> CompletableFuture.supplyAsync(project::getTaskList,
						createDaemonThreadPool(project.getTaskList().size())))
				.map(future -> future.thenApplyAsync(tasks -> tasks.stream()
						.flatMap(task -> task.getIssuesAsync(task, createDaemonThreadPool(tasks.size())).stream())
						.filter(issue -> issue.getStatus().equals(Status.fromName(status)))
						.collect(Collectors.toList())));
	}

	private Executor createDaemonThreadPool(int poolSize) {
		return Executors.newFixedThreadPool(poolSize, (Runnable r) -> {
			Thread t = new Thread(r);
			t.setDaemon(true);
			return t;
		});
	}
}