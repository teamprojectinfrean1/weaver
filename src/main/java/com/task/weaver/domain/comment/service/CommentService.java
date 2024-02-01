package com.task.weaver.domain.comment.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    Comment getComment(Long id) throws NotFoundException, AuthorizationException;

    Page<Comment> getComments (Long projectId, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Comment> getComments (Project project, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Page<Comment> getComments (Project project, Story story, Pageable pageable)
            throws NotFoundException, AuthorizationException;
    Comment addComment (String content, Story story, User user)
            throws AuthorizationException;
    Comment addComment(RequestCreateComment requestCreateComment)
            throws AuthorizationException;

    void deleteComment (Comment comment)
            throws NotFoundException, AuthorizationException;
    void deleteComment (Long commentId)
            throws NotFoundException, AuthorizationException;
    Comment updateComment(Comment originalComment, Comment newComment)
            throws NotFoundException, AuthorizationException;
    Comment updateComment(Long originalCommentId, Comment newComment)
            throws NotFoundException, AuthorizationException;

}
