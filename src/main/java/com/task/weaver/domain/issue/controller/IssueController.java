package com.task.weaver.domain.issue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.weaver.domain.issue.dto.request.CreateIssueRequest;
import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.service.IssueService;

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
	@GetMapping("/{id}")
	public ResponseEntity<?> getIssueInfo(@PathVariable Long issueId){
		IssueResponse issueResponse = issueService.getIssue(issueId);
		return ResponseEntity.ok()
			.body(issueResponse);
	}

	@Operation(summary = "이슈 생성", description = "이슈 생성")
	@PostMapping
	public ResponseEntity<?> addIssue(@RequestBody CreateIssueRequest createIssueRequest) {
		Long issueId = issueService.addIssue(createIssueRequest);
		issueService.addIssueStatus(issueId, createIssueRequest.statusId());
		return ResponseEntity.ok().body("Add Issue Successful");
	}

	@Operation(summary = "이슈 삭제", description = "issueId로 이슈 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeIssue(@PathVariable Long issueId){
		issueService.deleteIssue(issueId);
		return ResponseEntity.ok()
			.body("Delete Successful");
	}


}
