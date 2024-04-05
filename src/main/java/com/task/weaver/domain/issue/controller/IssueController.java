package com.task.weaver.domain.issue.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.dto.request.UpdateIssueRequest;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.service.IssueService;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Issue Controller", description = "이슈 관련 컨트롤러")
@RestController
@RequestMapping("/api/v1/issue")
@RequiredArgsConstructor
public class IssueController {

	private final IssueService issueService;
	// private final DataResponse dataResponse;

	@Operation(summary = "이슈 단건 조회", description = "issueId로 이슈 단건 조회")
	@GetMapping("/detail/{issueId}")
	public ResponseEntity<?> getIssueInfo(@PathVariable UUID issueId){
		IssueResponse issueResponse = issueService.getIssue(issueId);
		return ResponseEntity.ok()
			.body(issueResponse);
	}

	@Operation(summary = "이슈 생성", description = "이슈 생성")
	@PostMapping
	public ResponseEntity<?> addIssue(@RequestBody CreateIssueRequest createIssueRequest) {
		return ResponseEntity.ok().body(issueService.addIssue(createIssueRequest));
	}

	@Operation(summary = "이슈 삭제", description = "issueId로 이슈 삭제")
	@DeleteMapping("/{issueId}")
	public ResponseEntity<?> removeIssue(@PathVariable UUID issueId){
		// 담당자만 삭제 가능하도록 수정
		issueService.deleteIssue(issueId);
		return ResponseEntity.ok()
			.body("Delete Successful");
	}

	/**
	 * Pageable은 Query Parameter로 넘어온 데이터를 @ModelAttribute를 생략하고 받음
	 * @return
	 */
	@Operation(summary = "이슈 조회", description = "status로 이슈 조회 (TODO / INPROGRESS / DONE)")
	@GetMapping("/allTickets/{status}")
	public ResponseEntity<?> getIssues(@PathVariable String status, GetIssuePageRequest getIssuePageRequest){
		// issueService.getIssues(projectId, status, getIssuePageRequest);
		return ResponseEntity.ok().body(issueService.getIssues(status, getIssuePageRequest));
	}

	@Operation(summary = "이슈 수정", description = "issueId로 기존 이슈 상세 정보 수정")
	@PutMapping("/detail/{issueId}")
	public ResponseEntity<?> updateTask(@PathVariable UUID issueId, UpdateIssueRequest updateIssueRequest){
		return ResponseEntity.ok().body(issueService.updateIssue(issueId, updateIssueRequest));
	}

	@Operation(summary = "이슈 상태 수정", description = "issueId로 이슈 상세 상태 수정")
	@PutMapping("/status/{issueId}")
	public ResponseEntity<?> updateTask(@PathVariable UUID issueId, String status){
		issueService.updateIssueStatus(issueId, status);
		return ResponseEntity.ok().body("Update Issue Status");
	}

	@Operation(summary = "이슈 검색 조회", description = "filter로 이슈 검색 조회 (MANAGER / TASK / ISSUE)")
	@GetMapping("/search/{status}")
	public ResponseEntity<?> getSearchIssues(@PathVariable String status, @RequestParam("filter") String filter, @RequestParam("word") String word, GetIssuePageRequest getIssuePageRequest){
		return ResponseEntity.ok().body(issueService.getSearchIssues(status, filter, word, getIssuePageRequest));
	}
}