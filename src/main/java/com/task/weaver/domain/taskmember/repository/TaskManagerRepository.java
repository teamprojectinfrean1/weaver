package com.task.weaver.domain.taskmember.repository;

import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.taskmember.entity.TaskManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskManagerRepository  extends JpaRepository<TaskManager, Long> {
    Page<TaskManager> findByTask(Task task, Pageable pageable);

}
