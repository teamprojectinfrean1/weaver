package com.task.weaver.domain.issue.repository.dsl.impl;


import java.util.List;
import java.util.UUID;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.common.model.Status;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.entity.QIssue;
import com.task.weaver.domain.issue.repository.dsl.IssueRepositoryDsl;
import com.task.weaver.domain.task.entity.QTask;
import com.task.weaver.domain.task.entity.Task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class IssueRepositoryImpl implements IssueRepositoryDsl {
	private final JPAQueryFactory jpaQueryFactory;

	// @Override
// 	public Page<Issue> findBySearch(UUID projectId, String status, String filter, String word, Pageable pageable) {
// 		QTask qTask = QTask.task;
// 		QIssue qIssue = QIssue.issue;
//
// 		List<Issue> issueList = jpaQueryFactory.selectFrom(qIssue)
// 			.join(qTask)
// 			.on(qIssue.task.taskId.eq(qTask.taskId) // taskId 같은거끼리 join
// 				.and(qTask.project.projectId.eq(projectId)) // projectId랑 같고
// 				.and(qIssue.status.eq(Status.valueOf(status)))) // status 같아야함
// 			.fetch();
//
//
// 		switch(filter){
// 			case "ASSIGNEE":
// // filter - 담당자, 태스크명, 이슈명
// // 				where assignee.nickname like ("%word%");
// // 				where task.name like ("%word%");
// // 				where issue.name like ();
// 				// word - 검색어
// 				/**
// 				 * filter - "ASSIGNEE", "ISSUE", "TASK"
// 				 * word - "???"
// 				 * issue.assignee.id like ()
// 				 * issue.task.taskId like ()
// 				 * issue.name like ()
// 				 */
// 		}
//
// 		// List<Issue> content = jpaQueryFactory.selectFrom(qIssue)
//
// 	}
}
