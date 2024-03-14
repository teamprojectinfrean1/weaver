package com.task.weaver.domain.project.repository;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.dsl.ProjectRepositoryDsl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID>, ProjectRepositoryDsl {
    @Query("SELECT p FROM Project AS p WHERE p.user.nickname = :nickname")
    Optional<List<Project>> findProjectsByNickname(@Param("nickname") String nickname);

//    @Query("SELECT p FROM Project AS p WHERE p.user.id = :userId")
//    Optional<List<Project>> findProjectsByUserId(@Param("userId") String userId);
}
