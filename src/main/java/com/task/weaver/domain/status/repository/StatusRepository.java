package com.task.weaver.domain.status.repository;

import com.task.weaver.domain.status.entity.Status;
import com.task.weaver.domain.status.repository.dsl.StatusRepositoryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long>, StatusRepositoryDsl {
}
