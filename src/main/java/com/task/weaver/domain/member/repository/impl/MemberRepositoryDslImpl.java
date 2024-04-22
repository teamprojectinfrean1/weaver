package com.task.weaver.domain.member.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.entity.QMember;
import com.task.weaver.domain.member.repository.MemberRepositoryDsl;
import com.task.weaver.domain.userOauthMember.oauth.entity.QOauthUser;
import com.task.weaver.domain.userOauthMember.user.entity.QUser;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberRepositoryDslImpl extends QuerydslRepositorySupport implements MemberRepositoryDsl {

    public MemberRepositoryDslImpl() {
        super(Member.class);
    }


    @Override
    public Page<Object[]> findMembersByProject(final UUID projectId, final Pageable pageable) {

        QProjectMember qProjectMember = QProjectMember.projectMember;
        QMember qMember = QMember.member;
        QUser qUser = QUser.user;
        QOauthUser qOauthMember = QOauthUser.oauthUser;

        JPQLQuery<ProjectMember> jpqlQuery = from(qProjectMember);
        jpqlQuery.leftJoin(qMember).on(qProjectMember.project.projectId.eq(projectId));
        jpqlQuery.leftJoin(qUser).on(qMember.user.eq(qUser));
        jpqlQuery.leftJoin(qOauthMember).on(qMember.oauthMember.eq(qOauthMember));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(qMember, qUser, qOauthMember);
        tuple.offset(pageable.getOffset());
        tuple.limit(pageable.getPageSize());

        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, tuple);
        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount();

        return new PageImpl<>(
                result.stream().map(Tuple::toArray).collect(Collectors.toList()),
                pageable,
                count);
    }
}
