package com.task.weaver.domain.issue.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, IssueRepositoryDsl {
	Optional<Issue> findByIssueId(Long issueId);
}
