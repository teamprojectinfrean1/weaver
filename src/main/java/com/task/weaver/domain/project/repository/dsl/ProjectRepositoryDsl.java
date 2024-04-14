package com.task.weaver.domain.project.repository.dsl;

import com.task.weaver.domain.member.Member;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.useroauthmember.entity.UserOauthMember;
import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryDsl {
    Optional<List<Project>> findProjectsByMember(UserOauthMember member);
}
