package com.task.weaver.domain.member.repository.impl;

import com.querydsl.jpa.JPQLQuery;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.entity.QMember;
import com.task.weaver.domain.member.repository.MemberRepositoryDsl;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.entity.QProjectMember;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class MemberRepositoryDslImpl extends QuerydslRepositorySupport implements MemberRepositoryDsl {

    public MemberRepositoryDslImpl() {
        super(Member.class);
    }


    @Override
    public Page<Member> findMembersByProject(final UUID projectId, final Pageable pageable) {

        QProjectMember qProjectMember = QProjectMember.projectMember;
        QMember qMember = QMember.member;

        JPQLQuery<ProjectMember> jpqlQuery =
                from(qProjectMember)
                        .leftJoin(qProjectMember.member, qMember)
                        .where(qProjectMember.project.projectId.eq(projectId));

        jpqlQuery.offset(pageable.getOffset());
        jpqlQuery.limit(pageable.getPageSize());

        List<ProjectMember> result = jpqlQuery.fetch();
        long count = jpqlQuery.fetchCount();

        List<Member> members = result.stream()
                .map(ProjectMember::getMember).toList();

        return new PageImpl<>(members, pageable, count);
    }
}
