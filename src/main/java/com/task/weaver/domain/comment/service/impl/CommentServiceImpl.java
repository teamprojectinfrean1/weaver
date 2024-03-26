package com.task.weaver.domain.comment.service.impl;

import com.task.weaver.common.exception.NotFoundException;
import com.task.weaver.domain.comment.dto.request.CommentPageRequest;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.CommentListResponse;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.CommentRepository;
import com.task.weaver.domain.comment.service.CommentService;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final IssueRepository issueRepository;

    @Override
    public ResponseComment getComment(Long id) throws NotFoundException{
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("")); //예외추가
        return new ResponseComment(comment);

    }

    @Override
    public Page<CommentListResponse> getComments(UUID issueId, CommentPageRequest commentPageRequest) throws NotFoundException {
        if(issueId.equals(commentPageRequest.issueId())) throw new NotFoundException();
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        List<CommentListResponse> commentList = new ArrayList<>();
        Pageable pageable = commentPageRequest.getPageable(Sort.by("comment_id").descending());
        for(Comment comment : issue.getComments()){
            commentList
                    .add( new CommentListResponse(
                                    comment.getComment_id(),
                                    comment.getBody(),
                                    comment.getIssue().getIssueId()
                            )
                    );
        }
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), commentList.size());
        return new PageImpl<>(commentList.subList(start,end), pageRequest, commentList.size());
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
    public ResponseComment updateComment(Long originalCommentId, RequestUpdateComment requestUpdateComment) throws NotFoundException {
        Comment comment = commentRepository.findById(originalCommentId)
                .orElseThrow(() -> new IllegalArgumentException(""));
        User updater = userRepository.findById(requestUpdateComment.getUpdaterUUID())
                .orElseThrow(() -> new IllegalArgumentException(""));
        if(!validate(comment.getUser(),updater)){
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