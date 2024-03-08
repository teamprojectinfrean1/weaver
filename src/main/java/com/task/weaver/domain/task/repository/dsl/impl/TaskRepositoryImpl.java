package com.task.weaver.domain.task.repository.dsl.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.QTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.dsl.TaskRepositoryDsl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryDsl {
    QTask qTask = QTask.task;
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public Page<Task> findByProject(Project project, Pageable pageable) {
        List<Task> content = jpaQueryFactory.selectFrom(qTask)
                .where(qTask.project.eq(project))
                .orderBy(qTask.taskId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(qTask.count())
                .from(qTask)
                .where(qTask.project.eq(project))
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }


}
