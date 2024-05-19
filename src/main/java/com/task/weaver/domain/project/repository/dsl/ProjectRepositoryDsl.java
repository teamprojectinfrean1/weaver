package com.task.weaver.domain.project.repository.dsl;

import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.dto.request.GetIssuePageRequest;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProjectRepositoryDsl {
    Optional<List<Project>> findProjectsByMember(Member member);

    List<Issue> findIssueListByProjectId(final UUID projectId, final String status, final Pageable pageable);

    Page<Issue> findIssuePageByProjectId(final UUID projectId, final String status, final Pageable pageable);
}
