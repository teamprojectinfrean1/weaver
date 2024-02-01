package com.task.weaver.domain.comment.service;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {
    ResponseComment getComment(Long id);

    Page<Comment> getComments (Long projectId, Pageable pageable);
    Page<Comment> getComments (Project project, Pageable pageable);
    Page<Comment> getComments (Project project, Story story, Pageable pageable);
    Comment addComment (String content, Story story, User user);

    void deleteComment (Comment comment);
    void deleteComment (Long commentId);

    ResponseComment updateComment(Comment originalComment, RequestUpdateComment newComment);

    ResponseComment updateComment(Long originalCommentId, RequestUpdateComment newComment);
}
