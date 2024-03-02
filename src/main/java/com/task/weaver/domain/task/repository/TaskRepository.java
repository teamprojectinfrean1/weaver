package com.task.weaver.domain.task.repository;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.dsl.TaskRepositoryDsl;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TaskRepository extends JpaRepository<Task, Long>, TaskRepositoryDsl {
    Page<Task> findByProject(Project project, Pageable pageable);
    Page<Task> findByStatusTag(StatusTag statusTag, Pageable pageable);
    Page<Task> findByUser(User user, Pageable pageable);
    Page<Task> findByTitle(String taskName, Pageable pageable);
    Page<Task> findByContent(String detail, Pageable pageable);
//    Page<Task> findByStart_date(LocalDateTime start_date, Pageable pageable);

}