package com.task.weaver.domain.projectmember.repository.dsl;

import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectMemberRepositoryDsl {
    Optional<List<ProjectMember>> findProjectMemberByMember(Member member);

    void bulkDeleteProjectMembers(final Project project, final List<UUID> memberUuidList);

    boolean findByProjectAndMemberId(@Param("memberId") UUID memberId, @Param("projectId") UUID projectId);
}
