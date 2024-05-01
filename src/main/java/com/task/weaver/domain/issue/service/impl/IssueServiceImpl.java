package com.task.weaver.domain.issue.service.impl;

import static com.task.weaver.common.exception.ErrorCode.ISSUE_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.TASK_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.issue.IssueNotFoundException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.exception.task.TaskNotFoundException;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
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
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class IssueServiceImpl implements IssueService {
	private final IssueRepository issueRepository;
	private final MemberRepository memberRepository;
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;

	@Override
	public IssueResponse getIssueResponse(UUID issueId) {
		return new IssueResponse(getIssue(issueId));
	}

	private List<Issue> findIssuesByStatusAsync(final String status,
										final GetIssuePageRequest getIssuePageRequest) {

		Stream<CompletableFuture<List<Issue>>> completableFutureStream = findIssueStream(getIssuePageRequest, status);

		return completableFutureStream
				.map(CompletableFuture::join)
				.flatMap(List::stream)
				.collect(Collectors.toList());
	}

	/**
	 * TODO: 2024-04-29, 월, 1:27  -JEON
	 *  TASK: findById with EntityGraph version
	 */
	private Stream<CompletableFuture<List<Issue>>> findIssueStream(final GetIssuePageRequest getIssuePageRequest,
																   String status) {
		return projectRepository.findById(getIssuePageRequest.projectId()).stream()
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

	/**TODO: 2024-05-2, 목, 1:39  -JEON
	*  TASK: findIssueByProject with QueryDsl version
	*/
	private List<Issue> findIssuesByStatusQueryDsl(final String status,
												   final GetIssuePageRequest getIssuePageRequest) {
		return findIssueStreamWithQueryDsl(getIssuePageRequest, status);
	}

	private List<Issue> findIssueStreamWithQueryDsl(final GetIssuePageRequest getIssuePageRequest, String status) {
		return projectRepository.findIssueByProjectId(getIssuePageRequest.projectId(), status).orElseThrow();
	}

	@Override
	public ResponsePageResult<GetIssueListResponse, Issue> getIssues(String status,
																	 GetIssuePageRequest getIssuePageRequest) {

		log.info("status ={}, project id ={}", status, getIssuePageRequest.projectId());

//		List<Issue> issuesByStatusAsync = findIssuesByStatusAsync(status, getIssuePageRequest);
		List<Issue> issuesByStatusAsync = findIssuesByStatusQueryDsl(status, getIssuePageRequest);

		Pageable pageable = getIssuePageRequest.getPageable(Sort.by("issueId").descending());
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), issuesByStatusAsync.size());
		Page<Issue> issuePage = new PageImpl<>(issuesByStatusAsync.subList(start, end), getPageRequest(pageable),
				issuesByStatusAsync.size());

		Function<Issue, GetIssueListResponse> fn = GetIssueListResponse::of;

		return new ResponsePageResult<>(issuePage, fn);
	}

	private static PageRequest getPageRequest(final Pageable pageable) {
		return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
	}

	@Override
	public ResponsePageResult<GetIssueListResponse, Issue> getSearchIssues(String status, String filter, String word,
																		   GetIssuePageRequest getIssuePageRequest) {

		Project project = projectRepository.findById(getIssuePageRequest.projectId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		List<Issue> issueList = new ArrayList<>();

		Pageable pageable = getIssuePageRequest.getPageable(Sort.by("issueId").descending());

		switch (filter) {
			case "ASSIGNEE":
				for (Task task : project.getTaskList()) {
					for(Issue issue : task.getIssueList()){
						// manager 확인
						if(issue.getAssignee().resolveMemberByLoginType().getNickname().contains(word)){
							issueList.add(issue);
						}
					}
				}
				break;
			case "TASK":
				for (Task task : project.getTaskList()) {
					if(task.getTaskTitle().contains(word)){
						for(Issue issue : task.getIssueList()){
							issueList.add(issue);
						}
					}
				}
				break;
			case "ISSUE":
				for (Task task : project.getTaskList()) {
					for(Issue issue : task.getIssueList()){
						// issue title 확인
						if(issue.getIssueTitle().contains(word)){
							issueList.add(issue);
						}
					}
				}
				break;
		}

		// Page<Issue> issuePage = issueList;

		// paging 처리
		PageRequest pageRequest = getPageRequest(pageable);
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), issueList.size());
		Page<Issue> issuePage = new PageImpl<>(issueList.subList(start, end), pageRequest, issueList.size());

		Function<Issue, GetIssueListResponse> fn = GetIssueListResponse::of;
		return new ResponsePageResult<>(issuePage, fn);
	}

	@Override
	public IssueResponse addIssue(CreateIssueRequest createIssueRequest) {
		Task task = taskRepository.findById(createIssueRequest.taskId())
				.orElseThrow(() -> new TaskNotFoundException(TASK_NOT_FOUND, TASK_NOT_FOUND.getMessage()));
		Member creator = memberRepository.findById(createIssueRequest.creatorId())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
		Member assignee = memberRepository.findById(createIssueRequest.assigneeId())
				.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));

		Issue issue = Issue.builder()
			.task(task)
			.modifier(creator) // 생성자 & 수정자 역할
			.assignee(assignee) // 담당자
			.issueTitle(createIssueRequest.issueTitle())
			.issueContent(createIssueRequest.issueContent())
			.startDate(createIssueRequest.startDate())
			.endDate(createIssueRequest.endDate())
			.status(Status.fromName(createIssueRequest.status()))
			.build();
		issueRepository.save(issue);

		task.addIssue(issue);
		taskRepository.save(task);
		return new IssueResponse(issue);
	}

	@Override
	@Transactional
	public IssueResponse updateIssue(UUID issueId, UpdateIssueRequest updateIssueRequest) {

		// assignee만 수정 가능하게 ?
		// 수정할 때 DynamicUpdate를 사용 X (성능 오버헤드 발생) -> 더티체킹으로 ㄱㄱ

		Issue issue = getIssue(issueId);

		if (updateIssueRequest.taskId() != null) {
			Task task = taskRepository.findById(updateIssueRequest.taskId())
					.orElseThrow(() -> new IllegalArgumentException(""));
			issue.updateTask(task);
		}
		if(updateIssueRequest.assigneeId() != null){
			Member assignee = memberRepository.findById(updateIssueRequest.assigneeId())
				.orElseThrow(() -> new IllegalArgumentException(""));
			issue.updateAssignee(assignee);
		}
		if(updateIssueRequest.issueTitle() != null){
			issue.updateIssueTitle(updateIssueRequest.issueTitle());
		}
		if(updateIssueRequest.issueContent() != null){
			issue.updateIssueContent(updateIssueRequest.issueContent());
		}
		if(updateIssueRequest.startDate() != null){
			issue.updateStartDate(updateIssueRequest.startDate());
		}
		if(updateIssueRequest.endDate() != null){
			issue.updateEndDate(updateIssueRequest.endDate());
		}
		if(updateIssueRequest.status() != null){
			issue.updateStatus(Status.valueOf(updateIssueRequest.status()));
		}

		// modifier 변경
		Member modifier = memberRepository.findById(updateIssueRequest.modifierId())
			.orElseThrow(() -> new IllegalArgumentException(""));

		issue.updateModifier(modifier);

		return new IssueResponse(issue);
	}

	@Override
	public void updateIssueStatus(UUID issueId, String status) {
		Issue issue = getIssue(issueId);
		issue.updateStatus(Status.fromName(status));
	}

	private Issue getIssue(final UUID issueId) {
		return issueRepository.findById(issueId)
				.orElseThrow(() -> new IssueNotFoundException(ISSUE_NOT_FOUND, ISSUE_NOT_FOUND.getMessage()));
	}

	@Override
	public void deleteIssue(Issue issue) {
		issueRepository.delete(issue);
	}

	@Override
	public void deleteIssue(UUID issueId) {
		issueRepository.deleteById(issueId);
	}
}