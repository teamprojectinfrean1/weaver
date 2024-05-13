package com.task.weaver.domain.issue.controller;

import com.task.weaver.common.aop.annotation.HasTasks;
import com.task.weaver.common.aop.annotation.LoggingStopWatch;
import com.task.weaver.common.model.Status;
import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueStatusRequest;
import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.dto.response.UpdateIssueStatus;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "Issue Controller", description = "이슈 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/issue")
@RequiredArgsConstructor
public class IssueController {

	private final IssueService issueService;

	@Operation(summary = "이슈 단건 조회", description = "issueId로 이슈 단건 조회")
	@GetMapping("/detail/{issueId}")
	public ResponseEntity<DataResponse<IssueResponse>> getIssueInfo(@PathVariable UUID issueId) {
		IssueResponse issueResponse = issueService.getIssueResponse(issueId);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "이슈 상세 조회 성공", issueResponse, true), HttpStatus.OK);
	}

	@Operation(summary = "이슈 생성", description = "이슈 생성")
	@PostMapping
	public ResponseEntity<DataResponse<IssueResponse>> addIssue(@RequestBody CreateIssueRequest createIssueRequest) {
		return new ResponseEntity<>(
				DataResponse.of(HttpStatus.OK, "이슈 생성 성공", issueService.addIssue(createIssueRequest), true),
				HttpStatus.OK);
	}

	@Operation(summary = "이슈 삭제", description = "issueId로 이슈 삭제")
	@DeleteMapping("/{issueId}")
	public ResponseEntity<DataResponse<UUID>> removeIssue(@PathVariable UUID issueId){
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "이슈 삭제 성공", issueService.deleteIssue(issueId), true), HttpStatus.OK);
	}

	@LoggingStopWatch
	@Operation(summary = "이슈 조회", description = "status로 이슈 조회 (TODO / INPROGRESS / DONE)")
	@GetMapping("/allTickets/{status}")
	public ResponseEntity<DataResponse<ResponsePageResult<GetIssueListResponse, Issue>>> getIssues(
			@PathVariable Status status,
			@Valid @HasTasks @RequestParam UUID projectId,
			@RequestParam int page, @RequestParam int size) {
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "status로 이슈 조회 성공",
				issueService.getIssues(status, new GetIssuePageRequest(page, size, projectId)), true), HttpStatus.OK);
	}

	@Operation(summary = "이슈 검색 조회", description = "filter로 이슈 검색 조회 (MANAGER / TASK / ISSUE)")
	@GetMapping("/search/{status}")
	public ResponseEntity<DataResponse<ResponsePageResult<GetIssueListResponse, Issue>>> getSearchIssues(
			@PathVariable String status, @RequestParam("filter") String filter, @RequestParam("word") String word,
			@Valid @HasTasks @RequestParam UUID projectId, @RequestParam int page, @RequestParam int size) {
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "이슈 검색 조회 성공",
				issueService.getSearchIssues(status, filter, word, new GetIssuePageRequest(page, size, projectId)),
				true), HttpStatus.OK);
	}

	@Operation(summary = "이슈 수정", description = "issueId로 기존 이슈 상세 정보 수정")
	@PutMapping("/detail/{issueId}")
	public ResponseEntity<DataResponse<IssueResponse>> updateTask(@PathVariable UUID issueId,
																  @RequestBody UpdateIssueRequest updateIssueRequest) {
		return new ResponseEntity<>(
				DataResponse.of(HttpStatus.OK, "이슈 상세 정보 수정 성공", issueService.updateIssue(issueId, updateIssueRequest),
						true), HttpStatus.OK);
	}

	@Operation(summary = "이슈 상태 수정", description = "issueId로 이슈 상세 상태 수정")
	@PutMapping("/status/{issueId}")
	public ResponseEntity<DataResponse<UpdateIssueStatus>> updateTask(@PathVariable UUID issueId,
																	  @RequestBody UpdateIssueStatusRequest updateIssueStatusRequest) {
		UpdateIssueStatus updateIssueStatus = issueService.updateIssueStatus(issueId, updateIssueStatusRequest);
		return new ResponseEntity<>(DataResponse.of(HttpStatus.OK, "이슈 상태 수정 성공", updateIssueStatus, true),
				HttpStatus.OK);
	}
}