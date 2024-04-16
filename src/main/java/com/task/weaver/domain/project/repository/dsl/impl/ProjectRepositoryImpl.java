package com.task.weaver.domain.project.repository.dsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.authorization.entity.QMember;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.entity.QProject;
import com.task.weaver.domain.project.repository.dsl.ProjectRepositoryDsl;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.authorization.entity.Member;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QProjectMember qProjectMember = QProjectMember.projectMember;
    private QProject qProject = QProject.project;
    QMember qMember = QMember.member;

    @Override
    public Optional<List<Project>> findProjectsByMember(Member member) {
        List<Project> projects = jpaQueryFactory.selectFrom(qProject)
                .join(qProject.projectMemberList, qProjectMember)
                .where(qMember.loginType.eq(member.getLoginType()))
                .orderBy(qProject.created.desc())
                .fetch();

        return Optional.of(projects);
    }
}
