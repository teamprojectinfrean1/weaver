package com.task.weaver.domain.project.repository.dsl.impl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.entity.QIssue;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.entity.QProject;
import com.task.weaver.domain.project.repository.dsl.ProjectRepositoryDsl;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private final QProjectMember qProjectMember = QProjectMember.projectMember;
    private final QProject qProject = QProject.project;
    private final QIssue qIssue = QIssue.issue;

    @Override
    public Optional<List<Project>> findProjectsByMember(Member member) {
        QProjectMember qProjectMember = QProjectMember.projectMember;
        QProject qProject = QProject.project;

        List<Project> projects = jpaQueryFactory.selectFrom(qProject)
                .join(qProject.projectMemberList, qProjectMember).fetchJoin()
                .where(qProjectMember.member.id.eq(member.getId()))
                .orderBy(qProject.regDate.desc())
                .fetch();

        return Optional.of(projects);
    }

    @Override
    public Page<Issue> findIssuePageByProjectId(final UUID projectId, final String status, final Pageable pageable) {
        JPAQuery<Long> totalCount = findIssueCountByProjectId(projectId, status);
        List<Issue> result = findIssueListByProjectId(projectId, status, pageable);
        return PageableExecutionUtils.getPage(result, pageable, totalCount::fetchOne);
    }

    public JPAQuery<Long> findIssueCountByProjectId(final UUID projectId, final String status) {
        return jpaQueryFactory.select(qIssue.count())
                .from(qIssue)
                .where(predicate(projectId, status));
    }

    @Override
    public List<Issue> findIssueListByProjectId(final UUID projectId, final String status, final Pageable pageable) {
        OrderSpecifier<LocalDateTime> orderSpecifier = qIssue.modDate.desc();
        BooleanExpression expression = predicate(projectId, status);
        return jpaQueryFactory.selectFrom(qIssue)
                .where(expression)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression predicate(final UUID projectId, final String status) {
        return qIssue.task.project.projectId.eq(projectId).and(qIssue.status.eq(Status.fromName(status)));
    }
}
