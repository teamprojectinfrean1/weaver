package com.task.weaver.domain.project.repository.dsl.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.entity.QIssue;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.entity.QProject;
import com.task.weaver.domain.project.repository.dsl.ProjectRepositoryDsl;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.task.entity.QTask;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QProjectMember qProjectMember = QProjectMember.projectMember;
    private QProject qProject = QProject.project;
    private QTask qTask = QTask.task;
    private QIssue qIssue = QIssue.issue;

    @Override
    public Optional<List<Project>> findProjectsByMember(Member member) {
        List<Project> projects = jpaQueryFactory.selectFrom(qProject)
                .join(qProject.projectMemberList, qProjectMember)
                .where(qProjectMember.member.id.eq(member.getId()))
                .orderBy(qProject.regDate.desc())
                .fetch();

        return Optional.of(projects);
    }

    @Override
    public Optional<List<Issue>> findIssueByProjectId(final UUID projectId, final String status) {
        BooleanExpression expression = qIssue.task.project.projectId.eq(projectId)
                .and(qIssue.status.eq(Status.fromName(status)));
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qIssue)
                .where(expression)
                .fetch());
    }
}
