package com.task.weaver.domain.projectmember.repository;

import com.task.weaver.domain.projectmember.entity.ProjectMember;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, UUID> {

}
