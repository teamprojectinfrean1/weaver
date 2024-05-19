package com.task.weaver.domain.projectmember.repository;

import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.dsl.ProjectMemberRepositoryDsl;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID>, ProjectMemberRepositoryDsl {

    @EntityGraph(attributePaths = {"member"})
    @Query("SELECT pm FROM ProjectMember pm WHERE pm.project.projectId = :projectId")
    List<ProjectMember> findByProjectId(@Param("projectId") UUID projectId);
}
