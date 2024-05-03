package com.task.weaver.domain.projectmember.repository.dsl;

import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectMemberRepositoryDsl {
    Optional<List<ProjectMember>> findProjectsByMember(Member member);
}
