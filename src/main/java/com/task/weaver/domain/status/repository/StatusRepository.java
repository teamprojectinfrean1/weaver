package com.task.weaver.domain.status.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.weaver.domain.status.entity.Status;
import com.task.weaver.domain.status.repository.dsl.StatusRepositoryDsl;

public interface StatusRepository extends JpaRepository<Status, Long>, StatusRepositoryDsl {
}
