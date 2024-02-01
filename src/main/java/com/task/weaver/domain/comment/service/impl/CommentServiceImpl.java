package com.task.weaver.domain.comment.service.impl;

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
    public Page<Comment> getComments(Long projectId, Pageable pageable){
        return null;
    }

    @Override
    public Page<Comment> getComments(Project project, Pageable pageable){
        return null;
    }

    @Override
    public Page<Comment> getComments(Project project, Story story, Pageable pageable){
        return null;
    }

    @Override
    public Comment addComment(String content, Story story, User user){
        return null;
    }

    @Override
    public void deleteComment(Comment comment){
        commentRepository.delete(comment);
    }

    @Override
    public void deleteComment(Long commentId){
        commentRepository.delete(commentRepository.findById(commentId).get());
    }

    @Override
    public ResponseComment updateComment(Comment originalComment, RequestUpdateComment newComment){
        originalComment.updateComment(newComment);
        commentRepository.save(originalComment);
        return new ResponseComment(originalComment);
    }

    @Override
    public ResponseComment updateComment(Long originalCommentId, RequestUpdateComment newComment){
        Comment comment = commentRepository.findById(originalCommentId).get();
        comment.updateComment(newComment);
        commentRepository.save(comment);
        return new ResponseComment(comment);
    }
}
