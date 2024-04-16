package com.task.weaver.domain.user.repository.dsl.Impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.project.entity.QProject;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.entity.QUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.dsl.UserRepositoryDsl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

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
    public Page<User> findUsersForProject(UUID projectId, String nickname, Pageable pageable) {
        List<User> result = jpaQueryFactory
                .selectFrom(qUser)
                .join(qUser.projectMemberList, qProjectMember)
                .where(qProjectMember.project.projectId.eq(projectId), NicknameEq(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(qUser.count())
                .from(qUser)
                .join(qUser.projectMemberList, qProjectMember)
                .where(qProjectMember.project.projectId.eq(projectId))
                .fetchOne();

        return PageableExecutionUtils.getPage(result, pageable, () -> count);
    }

    BooleanExpression NicknameEq(String nickname){
        return nickname.isBlank() != true ? qUser.nickname.contains(nickname) : null;
    }
}
