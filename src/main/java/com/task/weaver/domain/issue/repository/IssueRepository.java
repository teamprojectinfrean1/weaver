package com.task.weaver.domain.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;

public interface IssueRepository extends JpaRepository<Long, Issue>, IssueRepositoryDsl {
}
