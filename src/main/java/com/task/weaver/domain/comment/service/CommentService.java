package com.task.weaver.domain.comment.service;

import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.dto.response.ResponseCommentList;
import com.task.weaver.domain.comment.dto.response.ResponseCommentUuid;
import com.task.weaver.domain.comment.dto.response.ResponsePageComment;
import com.task.weaver.domain.comment.entity.Comment;
import java.util.UUID;

public interface CommentService {
    ResponseComment fetchComment(UUID uuid);

    ResponseComment addComment(RequestCreateComment requestComment);

    void deleteComment(Comment comment);

    ResponsePageComment<ResponseCommentList, Comment> getComments(int page, int size, UUID issueId);

    ResponseCommentUuid deleteComment(UUID commentId);

    ResponseComment updateComment(UUID originalCommentId, RequestUpdateComment requestUpdateComment);
}