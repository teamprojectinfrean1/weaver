package com.task.weaver.domain.comment.service;

import com.task.weaver.domain.comment.dto.request.CommentPageRequest;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.dto.response.ResponseCommentList;
import com.task.weaver.domain.comment.dto.response.ResponsePageComment;
import com.task.weaver.domain.comment.entity.Comment;

public interface CommentService {
    ResponseComment getComment(Long id);

    ResponseComment addComment(RequestCreateComment requestComment);

    void deleteComment(Comment comment);

    ResponsePageComment<ResponseCommentList, Comment> getComments(CommentPageRequest commentPageRequest);

    void deleteComment(Long commentId);

    ResponseComment updateComment(Long originalCommentId, RequestUpdateComment requestUpdateComment);
}