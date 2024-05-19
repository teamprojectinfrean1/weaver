package com.task.weaver.domain.projectmember.repository.dsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.entity.QMember;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.projectmember.repository.dsl.ProjectMemberRepositoryDsl;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class ProjectMemberRepositoryImpl implements ProjectMemberRepositoryDsl {

    private final JPAQueryFactory jpaQueryFactory;
    private final QProjectMember qProjectMember = QProjectMember.projectMember;

    @Override
    public List<ProjectMember> findProjectMemberByMember(Member member) {
        QProjectMember qProjectMember = QProjectMember.projectMember;
        return jpaQueryFactory.selectFrom(qProjectMember)
                .leftJoin(qProjectMember.project)
                .where(qProjectMember.member.id.eq(member.getId()))
                .orderBy(qProjectMember.regDate.desc())
                .fetch();
    }

    @Override
    public void bulkDeleteProjectMembers(final Project project, final List<UUID> memberUuidList) {
        jpaQueryFactory
                .delete(qProjectMember)
                .where(qProjectMember.project.eq(project).and(qProjectMember.member.id.notIn(memberUuidList)))
                .execute();
    }

    @Override
    public boolean hasMatchedProjectAndMemberId(final UUID memberId, final UUID projectId) {
        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(qProjectMember)
                .where(qProjectMember.member.id.eq(memberId).and(qProjectMember.project.projectId.eq(projectId)))
                .fetchFirst();
        return fetchOne != null;
    }

    @Override
    public Page<ProjectMember> findProjectMemberPageByProjectId(final UUID projectId, final Pageable pageable) {
        List<ProjectMember> result = getProjectMembers(projectId);
        return getPage(result, pageable);
    }

    private List<ProjectMember> getProjectMembers(final UUID projectId) {
        QMember qMember = QMember.member;

        return jpaQueryFactory.selectFrom(qProjectMember)
                .leftJoin(qMember.user).fetchJoin()
                .leftJoin(qMember.oauthMember).fetchJoin()
                .leftJoin(qMember.assigneeIssueList).fetchJoin()
                .where(qProjectMember.project.projectId.eq(projectId))
                .distinct()
                .fetch();
    }

    private Page<ProjectMember> getPage(List<ProjectMember> members, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), members.size());
        return new PageImpl<>(members.subList(start, end), pageable, members.size());
    }

    @Override
    public boolean findByProjectAndMemberId(final UUID memberId, final UUID projectId) {
        QProjectMember qProjectMember = QProjectMember.projectMember;

        Integer fetchOne = jpaQueryFactory
                .selectOne()
                .from(qProjectMember)
                .where(qProjectMember.member.id.eq(memberId).and(qProjectMember.project.projectId.eq(projectId)))
                .fetchFirst();

        return fetchOne != null;
    }
}
