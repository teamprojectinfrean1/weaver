package com.task.weaver.domain.project.repository;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.dsl.ProjectRepositoryDsl;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, UUID>, ProjectRepositoryDsl {

//    @Query("SELECT p FROM Project AS p WHERE p.writer.nickname = :nickname")
//    Optional<List<Project>> findProjectsByNickname(@Param("nickname") String nickname);

//    @Query("SELECT p FROM Project AS p WHERE p.user.id = :userId")
//    Optional<List<Project>> findProjectsByUserId(@Param("userId") String userId);
}
