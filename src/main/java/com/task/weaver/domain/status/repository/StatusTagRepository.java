package com.task.weaver.domain.status.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.weaver.domain.status.entity.StatusTag;
import com.task.weaver.domain.status.repository.dsl.StatusTagRepositoryDsl;

public interface StatusTagRepository extends JpaRepository<StatusTag, Long>, StatusTagRepositoryDsl {
}
