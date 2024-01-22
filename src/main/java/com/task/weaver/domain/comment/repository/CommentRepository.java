package com.task.weaver.domain.comment.repository;

import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.dsl.CommentRepositoryDsl;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryDsl {
}
