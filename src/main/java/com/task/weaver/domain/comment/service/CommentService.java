package com.task.weaver.domain.comment.service;

import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.response.CommentListResponse;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;

public interface CommentService {
    ResponseComment getComment(Long id) throws NotFoundException;
    Page<CommentListResponse> getComments() throws NotFoundException;

    Long addComment (RequestCreateComment requestComment) throws NotFoundException;

    void deleteComment (Comment comment) throws NotFoundException;
    void deleteComment (Long commentId) throws NotFoundException;

    Comment updateComment(Comment originalComment, Comment newComment) throws NotFoundException;

    Comment updateComment(Long originalCommentId, Comment newComment) throws NotFoundException;
}
