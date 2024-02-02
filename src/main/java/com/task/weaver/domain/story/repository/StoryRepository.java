package com.task.weaver.domain.story.repository;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.story.repository.dsl.StoryRepositoryDsl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoryRepository extends JpaRepository<Story, Long>, StoryRepositoryDsl {

    Page<Story> findAllByProject (Pageable pageable, Project project);

}
