package com.task.weaver.domain.issue.repository;


import java.util.List;
import java.util.UUID;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;

public interface IssueRepository extends JpaRepository<Issue, UUID>, IssueRepositoryDsl {
	// Page<Issue> findAllByTask(Task task, Pageable pageable);

	// Page<Issue> findBySearch(UUID projectID, String status, String filter, String word, Pageable pageable);
}
