package com.task.weaver.domain.comment.service.impl;

import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.response.CommentListResponse;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.CommentRepository;
import com.task.weaver.domain.comment.service.CommentService;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    @Override
    public ResponseComment getComment(Long id) throws NotFoundException{
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("")); //예외추가
        return new ResponseComment(comment);

    }

    @Override
    public Page<CommentListResponse> getComments() throws NotFoundException {
//        User writer = userRepository.findById()
        return null;
    }

    @Override
    public Long addComment(RequestCreateComment requestComment) throws NotFoundException {
        User writer = userRepository.findById(requestComment.writerId())
                .orElseThrow(() -> new IllegalArgumentException(""));
        Issue issue = issueRepository.findById(requestComment.issueId())
                .orElseThrow(() -> new IllegalArgumentException(""));
        Comment comment = Comment.builder()
                .user(writer)
                .issue(issue)
                .body(requestComment.body())
                .create_date(requestComment.create_date())
                .build();
        return commentRepository.save(comment).getComment_id();
    }

    @Override
    public void deleteComment(Comment comment) throws NotFoundException {
        commentRepository.delete(comment);
    }

    @Override
    public void deleteComment(Long commentId) throws NotFoundException {
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment updateComment(Comment originalComment, Comment newComment) throws NotFoundException {
        return null;
    }

    @Override
    public Comment updateComment(Long originalCommentId, Comment newComment) throws NotFoundException {
        return null;
    }
}
