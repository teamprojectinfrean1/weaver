package com.task.weaver.domain.user.repository.dsl.Impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.project.entity.QProject;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.entity.QUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.dsl.UserRepositoryDsl;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryDsl {
    private final JPAQueryFactory jpaQueryFactory;
    QProjectMember qProjectMember = QProjectMember.projectMember;
    QUser qUser = QUser.user;
    QProject qProject = QProject.project;
    @Override
    public Optional<List<User>> findUsersForProject(UUID projectId) {
        List<User> result = jpaQueryFactory
                .selectFrom(qUser)
                .join(qUser.projectMemberList, qProjectMember)
                .where(qProjectMember.project.projectId.eq(projectId))
                .fetch();

        return Optional.of(result);
    }
}
