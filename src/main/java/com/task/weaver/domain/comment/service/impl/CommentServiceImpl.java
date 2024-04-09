package com.task.weaver.domain.comment.service.impl;

import com.task.weaver.common.exception.AuthorizationException;
import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.common.exception.comment.CommentNotFoundException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.comment.dto.request.CommentPageRequest;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.dto.response.ResponseCommentList;
import com.task.weaver.domain.comment.dto.response.ResponsePageComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.CommentRepository;
import com.task.weaver.domain.comment.service.CommentService;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;

import java.util.function.Function;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    @Override
    public ResponseComment getComment(Long id) throws NotFoundException {
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentNotFoundException(new Throwable(id.toString()))); //예외추가
        return new ResponseComment(comment);

    }

    @Override
    public ResponsePageComment<ResponseCommentList, Comment> getComments(CommentPageRequest commentPageRequest) throws NotFoundException, AuthorizationException {
        Issue issue = issueRepository.findById(commentPageRequest.getIssueId())
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(commentPageRequest.getIssueId()))));
        Pageable pageable = commentPageRequest.getPageable(Sort.by("issue_id").descending());
        Page<Comment> commentPage = commentRepository.findByIssue(issue, pageable);

        Function<Comment, ResponseCommentList> fn = Comment -> (new ResponseCommentList(Comment));
        return new ResponsePageComment<>(commentPage, fn);

    }

    @Override
    @Transactional
    public Long addComment(RequestCreateComment requestComment) throws NotFoundException {
        User writer = userRepository.findById(requestComment.writerId())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(requestComment.writerId().toString())));
        Issue issue = issueRepository.findById(requestComment.issueId())
                .orElseThrow(() -> new IllegalArgumentException(""));
        Comment comment = Comment.builder()
                .user(writer)
                .issue(issue)
                .body(requestComment.body())
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
    @Transactional
    public ResponseComment updateComment(Long originalCommentId, RequestUpdateComment requestUpdateComment) throws NotFoundException {
        Comment comment = commentRepository.findById(originalCommentId)
                .orElseThrow(() -> new CommentNotFoundException(new Throwable(originalCommentId.toString())));
        User updater = userRepository.findById(requestUpdateComment.getUpdaterUUID())
                .orElseThrow(() -> new UserNotFoundException(new Throwable(requestUpdateComment.getUpdaterUUID().toString())));
        if (!validate(comment.getUser(), updater)) {
            throw new NotFoundException();
        }
        Issue issue = issueRepository.findById(requestUpdateComment.getIssueId())
                .orElseThrow(() -> new IllegalArgumentException(""));
        comment.updateComment(requestUpdateComment, issue);
        ResponseComment responseComment
                = ResponseComment
                .builder()
                .commentId(comment.getComment_id())
                .body(requestUpdateComment.getCommentBody())
                .build();
        return responseComment;
    }

    private boolean validate(User user, User updater) {
        return user.getUserId().equals(updater.getUserId());
    }
}