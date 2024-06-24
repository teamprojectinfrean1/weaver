package com.task.weaver.domain.task.repository.dsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.QTask;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.dsl.TaskRepositoryDsl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;


@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepositoryDsl {
    QTask qTask = QTask.task;
    private final JPAQueryFactory jpaQueryFactory;

    //    @Override
//    public Page<Task> findByProject(Project project, Pageable pageable) {
//        List<Task> content = jpaQueryFactory.selectFrom(qTask)
//                .where(qTask.project.eq(project))
//                .orderBy(qTask.regDate.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();
//
//        Long count = jpaQueryFactory.select(qTask.count())
//                .from(qTask)
//                .where(qTask.project.eq(project))
//                .fetchOne();
//
//        return PageableExecutionUtils.getPage(content, pageable, () -> count);
//    }
    @Override
    public Page<Task> findByProject(Project project, Pageable pageable, int adjustedPage) {
        // 첫 페이지 이후의 offset을 조정
        long offset = (adjustedPage == 0) ? 0 : 7 + (adjustedPage - 1) * 8L;

        List<Task> content = jpaQueryFactory.selectFrom(qTask)
                .where(qTask.project.eq(project))
                .orderBy(qTask.regDate.desc())
                .offset(offset)
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(qTask.count())
                .from(qTask)
                .where(qTask.project.eq(project))
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> count);
    }
}
