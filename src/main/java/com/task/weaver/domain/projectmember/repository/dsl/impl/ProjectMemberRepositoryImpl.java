package com.task.weaver.domain.projectmember.repository.dsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.projectmember.repository.dsl.ProjectMemberRepositoryDsl;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProjectMemberRepositoryImpl implements ProjectMemberRepositoryDsl {

    private final JPAQueryFactory jpaQueryFactory;
    private QProjectMember qProjectMember = QProjectMember.projectMember;

    @Override
    public Optional<List<ProjectMember>> findProjectsByMember(Member member) {
        List<ProjectMember> projects = jpaQueryFactory.selectFrom(qProjectMember)
                .leftJoin(qProjectMember.project)
                .where(qProjectMember.member.id.eq(member.getId()))
                .orderBy(qProjectMember.regDate.desc())
                .fetch();
        return Optional.of(projects);
    }
}
