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
        List<Member> result = getMembers(projectId);
        return getPage(result, pageable);
    }

    @Override
    public List<Member> findMembersByProject(final UUID projectId) {
        return getMembers(projectId);
    }

    private List<Member> getMembers(final UUID projectId) {
        QProjectMember qProjectMember = QProjectMember.projectMember;
        QMember qMember = QMember.member;

        return queryFactory.selectFrom(qMember)
                .leftJoin(qMember.projectMemberList, qProjectMember).fetchJoin()
                .leftJoin(qMember.user).fetchJoin()
                .leftJoin(qMember.oauthMember).fetchJoin()
                .leftJoin(qMember.assigneeIssueList).fetchJoin()
                .where(qProjectMember.project.projectId.eq(projectId))
                .distinct()
                .fetch();
    }

    private Page<Member> getPage(List<Member> members, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), members.size());
        return new PageImpl<>(members.subList(start, end), pageable, members.size());
    }
}
