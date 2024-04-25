package com.task.weaver.domain.member.repository.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.entity.QMember;
import com.task.weaver.domain.member.repository.MemberRepositoryDsl;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepositoryDslImpl extends QuerydslRepositorySupport implements MemberRepositoryDsl {

    private final JPAQueryFactory queryFactory;

    public MemberRepositoryDslImpl(JPAQueryFactory jpaQueryFactory) {
        super(Member.class);
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Page<Member> findMembersByProject(final UUID projectId, final Pageable pageable) {

        QProjectMember qProjectMember = QProjectMember.projectMember;
        QMember qMember = QMember.member;

        // Main query
        List<Member> result = queryFactory.selectFrom(qMember)
                .leftJoin(qMember.projectMemberList, qProjectMember).fetchJoin()
                .leftJoin(qMember.user).fetchJoin()
                .leftJoin(qMember.oauthMember).fetchJoin()
                .leftJoin(qMember.modifierIssueList).fetchJoin()
                .leftJoin(qMember.assigneeIssueList).fetchJoin()
                .where(qProjectMember.project.projectId.eq(projectId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .distinct()
                .fetch();

        // Count query
        long total = queryFactory.selectFrom(qMember)
                .leftJoin(qMember.projectMemberList, qProjectMember)
                .where(qProjectMember.project.projectId.eq(projectId))
                .fetch().size();

        return new PageImpl<>(result, pageable, total);
    }
}
