package com.task.weaver.domain.comment.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.service.CommentService;
import com.task.weaver.domain.project.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Override
    public Comment getComment(Long id) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Comment> getComments(Long projectId, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Comment> getComments(Project project, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Page<Comment> getComments(Project project, Story story, Pageable pageable) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Comment addComment(String content, Story story, User user) throws AuthorizationException {
        return null;
    }

    @Override
    public Comment addComment(RequestCreateComment requestCreateComment) throws AuthorizationException {
        return null;
    }

    @Override
    public void deleteComment(Comment comment) throws NotFoundException, AuthorizationException {

    }

    @Override
    public void deleteComment(Long commentId) throws NotFoundException, AuthorizationException {

    }

    @Override
    public Comment updateComment(Comment originalComment, Comment newComment) throws NotFoundException, AuthorizationException {
        return null;
    }

    @Override
    public Comment updateComment(Long originalCommentId, Comment newComment) throws NotFoundException, AuthorizationException {
        return null;
    }
}
