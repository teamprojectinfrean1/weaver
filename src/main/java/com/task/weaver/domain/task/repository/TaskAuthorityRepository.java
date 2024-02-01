package com.task.weaver.domain.task.repository;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.task.entity.Task;
import com.task.weaver.domain.task.entity.TaskAuthority;
import com.task.weaver.domain.task.repository.dsl.TaskRepositoryDsl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskAuthorityRepository extends JpaRepository<TaskAuthority, Long>, TaskRepositoryDsl {



}