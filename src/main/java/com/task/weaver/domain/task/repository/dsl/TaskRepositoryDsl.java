package com.task.weaver.domain.task.repository.dsl;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskRepositoryDsl {
    Page<Task> findByProject(Project project, Pageable pageable, int adjustedPage);
}
