package com.task.weaver.domain.issue.repository.dsl.impl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepositoryDsl {

	private final JPAQueryFactory queryFactory;
}
