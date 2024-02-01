package com.task.weaver.domain.issue.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.weaver.domain.issue.dto.response.IssueResponse;
import com.task.weaver.domain.issue.service.IssueService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/issue")
@RequiredArgsConstructor
public class IssueController {

	private final IssueService issueService;
	// private final DataResponse dataResponse;

	@GetMapping("/{id}")
	public ResponseEntity<?> getIssueInfo(@PathVariable Long issueId){
		IssueResponse issueResponse = issueService.getIssue(issueId);
		return ResponseEntity.ok()
			.body(issueResponse);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<?> removeIssue(@PathVariable Long issueId){
		issueService.deleteIssue(issueId);
		return ResponseEntity.ok()
			.body("Delete Successful");
	}
}
