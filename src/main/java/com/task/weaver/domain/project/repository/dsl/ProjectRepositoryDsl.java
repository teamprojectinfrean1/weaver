package com.task.weaver.domain.project.repository.dsl;

import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepositoryDsl {
    Optional<List<Project>> findProjectsByMember(Member member);

    Optional<List<Issue>> findIssueByProjectId(UUID projectId, String status);
}
