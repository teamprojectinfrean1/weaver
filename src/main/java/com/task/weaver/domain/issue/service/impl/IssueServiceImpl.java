package com.task.weaver.domain.issue.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.member.user.repository.UserRepository;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.TaskRepository;
import com.task.weaver.domain.authorization.repository.MemberRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = false)
public class IssueServiceImpl implements IssueService {
	private final IssueRepository issueRepository;
	private final UserRepository userRepository;
	private final MemberRepository userOauthMemberRepository;
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;

	@Override
	public IssueResponse getIssue(UUID issueId) throws NotFoundException, AuthorizationException {
		Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException(""));
		return new IssueResponse(issue);
	}

	@Override
	public Page<GetIssueListResponse> getIssues(String status,
		GetIssuePageRequest getIssuePageRequest) throws NotFoundException, AuthorizationException {

		Project project = projectRepository.findById(getIssuePageRequest.projectId())
			.orElseThrow(() -> new IllegalArgumentException(""));

		List<GetIssueListResponse> issueList = new ArrayList<>();

		Pageable pageable = getIssuePageRequest.getPageable(Sort.by("issueId").descending());

		for (Task task : project.getTaskList()) {
			for(Issue issue : task.getIssueList()){
				// status 확인
				if(issue.getStatus().equals(Status.valueOf(status))){
					issueList.add(new GetIssueListResponse(issue.getIssueId(),
							issue.getTitle(),
							task.getTaskId(),
							task.getTaskTitle(),
							issue.getManager().getId().toString(),
							issue.getManager().getUser().getNickname(),
							issue.getManager().getUser().getProfileImage()));
				}
			}
		}

		// paging 처리 ..
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), issueList.size());
		Page<GetIssueListResponse> issuePage = new PageImpl<>(issueList.subList(start, end), pageRequest, issueList.size());

		return issuePage;
	}

	@Override
	public Page<GetIssueListResponse> getSearchIssues(String status, String filter, String word,
		GetIssuePageRequest getIssuePageRequest) throws NotFoundException, AuthorizationException {

		Project project = projectRepository.findById(getIssuePageRequest.projectId())
			.orElseThrow(() -> new IllegalArgumentException(""));

		List<GetIssueListResponse> issueList = new ArrayList<>();

		Pageable pageable = getIssuePageRequest.getPageable(Sort.by("issueId").descending());

		switch (filter){
			case "MANAGER":
				for (Task task : project.getTaskList()) {
					for(Issue issue : task.getIssueList()){
						// manager 확인
						if (issue.getManager().getUser().getNickname().contains(word)) {
							issueList.add(new GetIssueListResponse(issue.getIssueId(),
									issue.getTitle(),
									task.getTaskId(),
									task.getTaskTitle(),
									issue.getManager().getId().toString(),
									issue.getManager().getUser().getNickname(),
									issue.getManager().getUser().getProfileImage()));
						}
					}
				}
				break;
			case "TASK":
				for (Task task : project.getTaskList()) {
					if(task.getTaskTitle().contains(word)){
						for(Issue issue : task.getIssueList()){
							issueList.add(new GetIssueListResponse(issue.getIssueId(),
									issue.getTitle(),
									task.getTaskId(),
									task.getTaskTitle(),
									issue.getManager().getId().toString(),
									issue.getManager().getUser().getNickname(),
									issue.getManager().getUser().getProfileImage()));
						}
					}
				}
				break;
			case "ISSUE":
				for (Task task : project.getTaskList()) {
					for(Issue issue : task.getIssueList()){
						// issue title 확인
						if(issue.getTitle().contains(word)){
							issueList.add(new GetIssueListResponse(issue.getIssueId(),
								issue.getTitle(),
								task.getTaskId(),
								task.getTaskTitle(),
									issue.getManager().getId().toString(),
									issue.getManager().getUser().getNickname(),
									issue.getManager().getUser().getProfileImage()));
						}
					}
				}
				break;
		}

		// paging 처리
		PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), issueList.size());
		Page<GetIssueListResponse> issuePage = new PageImpl<>(issueList.subList(start, end), pageRequest, issueList.size());

		return issuePage;
	}

	@Override
	public UUID addIssue(CreateIssueRequest createIssueRequest) throws AuthorizationException {
		Task task = taskRepository.findById(createIssueRequest.taskId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		Member creator = userOauthMemberRepository.findById(createIssueRequest.creatorId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		Member manager = userOauthMemberRepository.findById(createIssueRequest.managerId())
			.orElseThrow(() -> new IllegalArgumentException(""));

		Issue issue = Issue.builder()
			.task(task)
			.creator(creator)
			.manager(manager)
			.title(createIssueRequest.title())
			.content(createIssueRequest.content())
			.startDate(createIssueRequest.startDate())
			.endDate(createIssueRequest.endDate())
			.status(Status.valueOf(createIssueRequest.status()))
			.build();
		return issueRepository.save(issue).getIssueId();
	}

	@Override
	@Transactional
	public IssueResponse updateIssue(UUID issueId, UpdateIssueRequest updateIssueRequest) throws
		NotFoundException,
		AuthorizationException {

		Issue issue = issueRepository.findById(issueId)
			.orElseThrow(() -> new IllegalArgumentException(""));
		Task task = taskRepository.findById(updateIssueRequest.taskId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		Member modifier = userOauthMemberRepository.findById(updateIssueRequest.modifierId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		Member manager = userOauthMemberRepository.findById(updateIssueRequest.managerId())
			.orElseThrow(() -> new IllegalArgumentException(""));

		issue.updateIssue(updateIssueRequest, task, modifier, manager);

		return new IssueResponse(issue);
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
