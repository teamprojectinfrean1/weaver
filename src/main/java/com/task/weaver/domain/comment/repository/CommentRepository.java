package com.task.weaver.domain.comment.repository;

import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.dsl.CommentRepositoryDsl;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryDsl {

    Page<Comment> findAllByProject (Project project, Pageable pageable);
    Page<Comment> findAllByProjectId (Long projectId, Pageable pageable);
    Page<Comment> findAllByProjectAndStory (Project project, Story story, Pageable pageable);
}
