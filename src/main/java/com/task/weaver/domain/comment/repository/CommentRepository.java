package com.task.weaver.domain.comment.repository;

import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.dsl.CommentRepositoryDsl;
import com.task.weaver.domain.issue.entity.Issue;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CommentRepositoryDsl {
    Page<Comment> findByIssue(Issue issue, Pageable pageable);
}
