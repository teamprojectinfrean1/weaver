package com.task.weaver.domain.task.repository;

import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.task.repository.dsl.TaskRepositoryDsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAuthorityRepository extends JpaRepository<TaskAuthority, Long>, TaskRepositoryDsl {



}