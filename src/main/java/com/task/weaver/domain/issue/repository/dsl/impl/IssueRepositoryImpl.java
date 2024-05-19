package com.task.weaver.domain.issue.repository.dsl.impl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.entity.QIssue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepositoryDsl {
	private final JPAQueryFactory jpaQueryFactory;
	private final QIssue issue = QIssue.issue;

	@Override
	public Page<Issue> findBySearch(final UUID projectId, final String status, final String filter, final String word, final Pageable pageable) {
		BooleanBuilder predicate = getBooleanBuilder(projectId, status, filter.trim(), word.trim());

		List<Issue> issues = jpaQueryFactory
				.selectFrom(issue)
				.where(predicate)
				.orderBy(issue.modDate.desc())
				.offset((int) pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetch();

		long total = jpaQueryFactory
				.selectFrom(issue)
				.where(predicate)
				.stream()
				.count();

		return new PageImpl<>(issues, pageable, total);
	}

	private BooleanBuilder getBooleanBuilder(final UUID projectId, final String status, final String filter,
											 final String word) {
		BooleanBuilder predicate = new BooleanBuilder();

		predicate.and(issue.task.project.projectId.eq(projectId))
				.and(issue.status.eq(Status.fromName(status)));

		hasFilterEqManager(filter, word, predicate);
		hasFilterEqTask(filter, word, predicate);
		hasFilterEqIssue(filter, word, predicate);
		return predicate;
	}

	private void hasFilterEqIssue(final String filter, final String word, final BooleanBuilder predicate) {
		if (filter.equalsIgnoreCase("ISSUE")) {
			predicate.and(issue.issueTitle.containsIgnoreCase(word));
		}
	}

	private void hasFilterEqManager(final String filter, final String word, final BooleanBuilder predicate) {
		if (isManagerTypeUser(filter)) {
			predicate.and(issue.assignee.user.nickname.containsIgnoreCase(word));
		} else if (isManagerTypeOauthMember(filter)) {
			predicate.and(issue.assignee.oauthMember.nickname.containsIgnoreCase(word));
		}
	}

	private void hasFilterEqTask(final String filter, final String word, final BooleanBuilder predicate) {
		if (filter.equalsIgnoreCase("TASK")) {
			predicate.and(issue.task.taskTitle.containsIgnoreCase(word));
		}
	}

	private boolean isManagerTypeOauthMember(final String filter) {
		return filter.equalsIgnoreCase("MANAGER") && issue.assignee.oauthMember != null;
	}

	private boolean isManagerTypeUser(final String filter) {
		return filter.equalsIgnoreCase("MANAGER") && issue.assignee.user != null;
	}
}
