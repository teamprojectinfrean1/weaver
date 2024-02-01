package com.task.weaver.domain.issue.repository;


import com.task.weaver.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;

public interface IssueRepository extends JpaRepository<Issue, Long>, IssueRepositoryDsl {
	// Optional<Issue> findByIssueId(Long issueId);

	// Page<Issue> findAllBy

	Page<Issue> findAllByTask(Task task, Pageable pageable);

//	Page<Issue> findAllBytaskId(Long taskId, Pageable pageable);

	// Page<Issue>	findAllBytaskId
}
