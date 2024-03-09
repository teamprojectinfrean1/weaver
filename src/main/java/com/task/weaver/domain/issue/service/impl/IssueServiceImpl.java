package com.task.weaver.domain.issue.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
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
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;

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
	private final UserRepository userRepository;
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;

	@Override
	public IssueResponse getIssue(Long issueId) throws NotFoundException, AuthorizationException {
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
			// status 확인 해야함
			for(Issue issue : task.getIssueList()){
				issueList.add(new GetIssueListResponse(issue.getIssueId(),
					issue.getTitle(),
					task.getTaskId(),
					task.getTaskTitle(),
					issue.getManager().getId(),
					issue.getManager().getNickname(),
					issue.getManager().getProfileImage()));
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
	public Long addIssue(CreateIssueRequest createIssueRequest) throws AuthorizationException {
		Task task = taskRepository.findById(createIssueRequest.taskId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		User creator = userRepository.findById(createIssueRequest.creatorId())
			.orElseThrow(() -> new IllegalArgumentException(""));
		User manager = userRepository.findById(createIssueRequest.managerId())
			.orElseThrow(() -> new IllegalArgumentException(""));

		Issue issue = Issue.builder()
			.task(task)
			.creator(creator)
			.manager(manager)
			.title(createIssueRequest.title())
			.content(createIssueRequest.content())
			.startDate(createIssueRequest.startDate())
			.endDate(createIssueRequest.endDate())
			.visible(false)
			.status(Status.valueOf(createIssueRequest.status()))
			.build();
		return issueRepository.save(issue).getIssueId();
	}

	@Override
	public Issue updateIssue(Issue originalIssue, Issue newIssue) throws NotFoundException, AuthorizationException {
		return null;
	}

	@Override
	public Issue updateIssue(Long originalIssueId, Issue newIssue) throws NotFoundException, AuthorizationException {
		return null;
	}

	@Override
	public void deleteIssue(Issue issue) throws NotFoundException, AuthorizationException {
		issueRepository.delete(issue);
	}

	@Override
	public void deleteIssue(Long issueId) throws NotFoundException, AuthorizationException {
		issueRepository.deleteById(issueId);
	}
}
