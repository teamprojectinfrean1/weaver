package com.task.weaver.domain.project.repository.dsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.entity.QProject;
import com.task.weaver.domain.project.repository.dsl.ProjectRepositoryDsl;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.member.user.entity.User;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepositoryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    private QProjectMember qProjectMember = QProjectMember.projectMember;
    private QProject qProject = QProject.project;
    @Override
    public Optional<List<Project>> findProjectsByUser(User user) {
        List<Project> projects = jpaQueryFactory.selectFrom(qProject)
                .join(qProject.projectMemberList, qProjectMember)
                .where(qProjectMember.user.eq(user))
                .orderBy(qProject.created.desc())
                .fetch();

        return Optional.of(projects);
    }
}
