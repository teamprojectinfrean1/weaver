package com.task.weaver.domain.issue.repository.dsl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.task.weaver.domain.issue.entity.Issue;

public interface IssueRepositoryDsl {
	// Page<Issue> findBySearch(UUID projectId, String status, String filter, String word, Pageable pageable);
	// Optional<List<Issue>> findIssuesByFilter()
}
