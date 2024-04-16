package com.task.weaver.domain.issue.service.impl;

import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.authorization.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class IssueServiceImpl implements IssueService {
	private final IssueRepository issueRepository;
	private final MemberRepository memberRepository;
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;

	@Override
	public IssueResponse getIssue(UUID issueId) throws NotFoundException, AuthorizationException {
		Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException(""));

		IssueResponse issueResponse = new IssueResponse(issue);

		return issueResponse;
	}

	/**
	 * 수정 필요
	 */
	@Override
	public ResponsePageResult<GetIssueListResponse, Issue> getIssues(String status,
																	 GetIssuePageRequest getIssuePageRequest) throws NotFoundException, AuthorizationException {

		Project project = projectRepository.findById(getIssuePageRequest.projectId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		List<Issue> issueList = new ArrayList<>();

		Pageable pageable = getIssuePageRequest.getPageable(Sort.by("issueId").descending());

		for (Task task : project.getTaskList()) {
			for(Issue issue : task.getIssueList()){
				// status 확인
				if(issue.getStatus().equals(Status.valueOf(status))){
					issueList.add(issue);
				}
			}
		}

		// paging 처리 ..
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), issueList.size());
		Page<Issue> issuePage = new PageImpl<>(issueList.subList(start, end), pageRequest, issueList.size());

//		Function<Issue, GetIssueListResponse> fn = Issue -> (new GetIssueListResponse(Issue.getIssueId(), Issue.getIssueTitle(),
//				Issue.getTask().getTaskId(), Issue.getTask().getTaskTitle(), Issue.getAssignee().getId(),
//				Issue.getAssignee().getNickname(), Issue.getAssignee().getProfileImage()));

		return null;
	}

	/**
	 * 수정 필요
	 */
	@Override
	public ResponsePageResult<GetIssueListResponse, Issue> getSearchIssues(String status, String filter, String word,
																		   GetIssuePageRequest getIssuePageRequest) throws NotFoundException, AuthorizationException {

		Project project = projectRepository.findById(getIssuePageRequest.projectId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		List<Issue> issueList = new ArrayList<>();

		Pageable pageable = getIssuePageRequest.getPageable(Sort.by("issueId").descending());

		// Page<Issue> issuePage = issueRepository.findBySearch(getIssuePageRequest.projectId(), status, filter, word, pageable);

		switch (filter){
			case "ASSIGNEE":
				for (Task task : project.getTaskList()) {
					for(Issue issue : task.getIssueList()){
						// manager 확인
//						if(issue.getAssignee().getNickname().contains(word)){
//							issueList.add(issue);
//						}
						issueList.add(issue);
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
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), issueList.size());
		Page<Issue> issuePage = new PageImpl<>(issueList.subList(start, end), pageRequest, issueList.size());

//
//		Function<Issue, GetIssueListResponse> fn = Issue -> (new GetIssueListResponse(Issue.getIssueId(), Issue.getIssueTitle(), Issue.getTask().getTaskId(),
//				Issue.getTask().getTaskTitle(), Issue.getAssignee().getId(),
//				Issue.getAssignee().getNickname(), Issue.getAssignee().getProfileImage()));

		// ResponsePageResult<> result = new ResponsePageResult()

		// Function<Issue, GetIssueListResponse> fn = Issue -> (new GetIssueListResponse(Issue));
//		return new ResponsePageResult<>(issuePage, fn);
		return null;
	}

	@Override
	public IssueResponse addIssue(CreateIssueRequest createIssueRequest) throws AuthorizationException {
		Task task = taskRepository.findById(createIssueRequest.taskId())
				.orElseThrow(() -> new IllegalArgumentException(""));
		Member creator = memberRepository.findById(createIssueRequest.creatorId())
				.orElseThrow(() -> new IllegalArgumentException(""));
		Member assignee = memberRepository.findById(createIssueRequest.managerId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		Issue issue = Issue.builder()
				.task(task)
				.modifier(creator)
				.assignee(assignee)
				.issueTitle(createIssueRequest.title())
				.issueContent(createIssueRequest.content())
				.startDate(createIssueRequest.startDate())
				.endDate(createIssueRequest.endDate())
				.status(Status.valueOf(createIssueRequest.status()))
				.build();
		issueRepository.save(issue).getIssueId();

		return new IssueResponse(issue);
	}

	@Override
	@Transactional
	public IssueResponse updateIssue(UUID issueId, UpdateIssueRequest updateIssueRequest) throws
			NotFoundException,
			AuthorizationException {

		// assignee만 수정 가능하게 ?
		// 수정할 때 DynamicUpdate를 사용 X (성능 오버헤드 발생) -> 더티체킹으로 ㄱㄱ

		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IllegalArgumentException(""));

		if(updateIssueRequest.taskId() != null){
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

		// modifier 변경
		Member modifier = memberRepository.findById(updateIssueRequest.modifierId())
				.orElseThrow(() -> new IllegalArgumentException(""));

		issue.updateModifier(modifier);

		return new IssueResponse(issue);
	}

	@Override
	public void updateIssueStatus(UUID issueId, String status) throws NotFoundException, AuthorizationException {
		Issue issue = issueRepository.findById(issueId)
				.orElseThrow(() -> new IllegalArgumentException(""));

		issue.updateStatus(Status.valueOf(status));
	}

	@Override
	public void deleteIssue(Issue issue) throws NotFoundException, AuthorizationException {
		issueRepository.delete(issue);
	}

	@Override
	public void deleteIssue(UUID issueId) throws NotFoundException, AuthorizationException {
		issueRepository.deleteById(issueId);
	}
}
