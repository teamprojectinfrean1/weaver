package com.task.weaver.domain.project.repository;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.dsl.ProjectRepositoryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectRepositoryDsl {
}
