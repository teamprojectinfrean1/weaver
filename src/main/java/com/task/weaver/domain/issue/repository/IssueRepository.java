package com.task.weaver.domain.issue.repository;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;

public interface IssueRepository extends JpaRepository<Issue, UUID>, IssueRepositoryDsl {

//    @EntityGraph(attributePaths = {"task", "assignee", "modifier"})
//    @Override
//    Optional<Issue> findById(UUID uuid);

    @EntityGraph(attributePaths = {"task", "assignee.user", "assignee.oauthMember", "modifier.user", "modifier.oauthMember"})
    @Override
    Optional<Issue> findById(UUID uuid);

    @Query("SELECT i, a.user, a.oauthMember, m.user, m.oauthMember, t "
            + "FROM Issue i "
            + "LEFT JOIN i.assignee a "
            + "LEFT JOIN a.user u1 "
            + "LEFT JOIN a.oauthMember o1 "
            + "LEFT JOIN i.modifier m "
            + "LEFT JOIN m.user u2 "
            + "LEFT JOIN m.oauthMember o2 "
            + "LEFT JOIN i.task t "
            + "WHERE i.issueId = :id")
    Optional<Issue> findByIdWithDetails(@Param("id") UUID id);

}
