package com.task.weaver.domain.projectmember.repository.dsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.projectmember.repository.dsl.ProjectMemberRepositoryDsl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectMemberRepositoryImpl implements ProjectMemberRepositoryDsl {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<ProjectMember>> findProjectMemberByMember(Member member) {
        QProjectMember qProjectMember = QProjectMember.projectMember;
        List<ProjectMember> projects = jpaQueryFactory.selectFrom(qProjectMember)
                .leftJoin(qProjectMember.project)
                .where(qProjectMember.member.id.eq(member.getId()))
                .orderBy(qProjectMember.regDate.desc())
                .fetch();
        return Optional.of(projects);
    }

    @Override
    public void bulkDeleteProjectMembers(final Project project, final List<UUID> memberUuidList) {
        QProjectMember qProjectMember = QProjectMember.projectMember;
        jpaQueryFactory
                .delete(qProjectMember)
                .where(qProjectMember.project.eq(project).and(qProjectMember.member.id.notIn(memberUuidList)))
                .execute();
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
