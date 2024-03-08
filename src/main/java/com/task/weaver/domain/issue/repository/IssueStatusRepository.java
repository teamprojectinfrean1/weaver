package com.task.weaver.domain.issue.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.weaver.domain.issue.entity.IssueStatus;
import com.task.weaver.domain.issue.repository.dsl.IssueStatusRepositoryDsl;

public interface IssueStatusRepository extends JpaRepository<IssueStatus, Long>, IssueStatusRepositoryDsl {
}
