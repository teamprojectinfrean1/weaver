package com.task.weaver.domain.issue.repository.dsl.impl;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.entity.QIssue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;
import com.task.weaver.domain.userOauthMember.LoginType;
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

		BooleanBuilder predicate = getBooleanBuilder(projectId, status, filter, word);

		List<Issue> issues = jpaQueryFactory
				.selectFrom(issue)
				.where(predicate)
				.offset((int) pageable.getOffset())
				.orderBy(issue.modDate.desc())
				.limit(pageable.getPageSize())
				.fetch();

		long total = jpaQueryFactory
				.selectFrom(issue)
				.where(predicate)
				.stream().count();

		return new PageImpl<>(issues, pageable, total);
	}

	private BooleanBuilder getBooleanBuilder(final UUID projectId, final String status, final String filter,
											 final String word) {
		BooleanBuilder predicate = new BooleanBuilder();
		predicate.and(projectIdEq(projectId))
				.and(statusEq(status));
		hasContainsIssue(filter, word, predicate);
		hasContainsTask(filter, word, predicate);
		hasContainsOAuthAssignee(filter, word, predicate);
		hasContainsUserAssignee(filter, word, predicate);
		return predicate;
	}

	private void hasContainsOAuthAssignee(final String filter, final String word, final BooleanBuilder predicate) {
		if (filter.equalsIgnoreCase("MANAGER") && issue.assignee.loginType.equals(LoginType.OAUTH)) {
			predicate.and(oauthNameEq(word));
		}
	}

	private void hasContainsUserAssignee(final String filter, final String word, final BooleanBuilder predicate) {
		if (filter.equalsIgnoreCase("MANAGER") && issue.assignee.loginType.equals(LoginType.WEAVER)) {
			predicate.and(userNameEq(word));
		}
	}

	private void hasContainsTask(final String filter, final String word, final BooleanBuilder predicate) {
		if (filter.equalsIgnoreCase("TASK")) {
			predicate.and(taskTitleEq(word));
		}
	}

	private void hasContainsIssue(final String filter, final String word, final BooleanBuilder predicate) {
		if (filter.equalsIgnoreCase("ISSUE")) {
			predicate.and(issueTitleEq(word));
		}
	}

	private BooleanExpression projectIdEq(final UUID projectId) {
		return issue.task.project.projectId.eq(projectId);
	}

	private BooleanExpression statusEq(final String status) {
		return issue.status.eq(Status.fromName(status));
	}

	private BooleanExpression issueTitleEq(final String title) {
		return issue.issueTitle.containsIgnoreCase(title);
	}

	private BooleanExpression taskTitleEq(final String title) {
		return issue.task.taskTitle.containsIgnoreCase(title);
	}

	private BooleanExpression oauthNameEq(final String nickname) {
		return issue.assignee.oauthMember.nickname.containsIgnoreCase(nickname);
	}

	private BooleanExpression userNameEq(final String nickname) {
		return issue.assignee.user.nickname.containsIgnoreCase(nickname);
	}
}
