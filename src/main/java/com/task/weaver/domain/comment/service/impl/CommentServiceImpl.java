package com.task.weaver.domain.comment.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.CommentRepository;
import com.task.weaver.domain.comment.service.CommentService;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    @Override
    public ResponseComment getComment(Long id){
        Comment comment = commentRepository.findById(id).get(); //예외추가
        return new ResponseComment(comment);

    }

    @Override
    public Comment addComment(String content, Story story, User user){
        Comment comment = Comment.builder()
                .body(content)
                .story(story)
                .user(user)
                .build();
        return commentRepository.save(comment);
    }

    @Override
    public void deleteComment(User deleter, Comment comment){
        validateOwner(deleter, comment);
        commentRepository.delete(comment);
    }

    @Override
    public void deleteComment(User deleter, Long commentId){
        validateOwner(deleter, commentRepository.findById(commentId).get());
        commentRepository.delete(commentRepository.findById(commentId).get());
    }

    @Override
    public ResponseComment updateComment(User updater, Comment originalComment, RequestUpdateComment newComment){
        validateOwner(updater, originalComment);
        originalComment.updateComment(newComment);
        return new ResponseComment(originalComment);
    }

    @Override
    public ResponseComment updateComment(User updater, Long originalCommentId, RequestUpdateComment newComment){
        Comment comment = commentRepository.findById(originalCommentId).get();
        validateOwner(updater, comment);
        comment.updateComment(newComment);
        return new ResponseComment(comment);
    }
    private void validateOwner (User user, Comment comment) throws AuthorizationException {
        if (user != comment.getUser()) {
            throw new AuthorizationException();
        }
    }
}
