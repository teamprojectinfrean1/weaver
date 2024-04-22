package com.task.weaver.domain.task.repository;

import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.repository.dsl.TaskRepositoryDsl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, TaskRepositoryDsl {
    Page<Task> findByProject(Project project, Pageable pageable);
    Page<Task> findByStatus(String status, Pageable pageable);
    Page<Task> findByMember(Member member, Pageable pageable);
    Page<Task> findByTaskTitle(String taskName, Pageable pageable);
    Page<Task> findByTaskContent(String detail, Pageable pageable);
//    Page<Task> findBystartDate(LocalDateTime startDate, Pageable pageable);
}