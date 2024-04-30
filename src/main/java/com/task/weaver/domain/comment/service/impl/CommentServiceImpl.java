package com.task.weaver.domain.comment.service.impl;

import static com.task.weaver.common.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.INVALID_INPUT_VALUE;
import static com.task.weaver.common.exception.ErrorCode.ISSUE_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.MISMATCHED_MEMBER;
import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.comment.CommentNotFoundException;
import com.task.weaver.common.exception.issue.IssueNotFoundException;
import com.task.weaver.common.exception.member.MismatchedMember;
import com.task.weaver.common.exception.member.UserNotFoundException;
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
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final IssueRepository issueRepository;

    @Override
    public ResponseComment getComment(UUID uuid) {
        Comment comment = commentRepository.findById(uuid)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));
        return new ResponseComment(comment);

    }

    @Override
    public ResponsePageComment<ResponseCommentList, Comment> getComments(int page, int size, UUID issueId) {
        log.info("Issue ID={}", issueId);
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException(ISSUE_NOT_FOUND, ISSUE_NOT_FOUND.getMessage()));

        Pageable pageable = getPageable(Sort.by("commentId").descending(), page, size);
        Page<Comment> comments = commentRepository.findByIssue(issue, pageable);
        Function<Comment, ResponseCommentList> fn = ResponseCommentList::new;
        return new ResponsePageComment<>(comments, fn);
    }

    private Pageable getPageable(Sort sort, int page, int size) {
        return PageRequest.of(page - 1, size, sort);
    }

    @Override
    @Transactional
    public ResponseComment addComment(RequestCreateComment requestComment) {
        Member writer = memberRepository.findById(requestComment.writerId())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_EMAIL_NOT_FOUND.getMessage()));
        Issue issue = issueRepository.findById(requestComment.issueId())
                .orElseThrow(() -> new IssueNotFoundException(ISSUE_NOT_FOUND, INVALID_INPUT_VALUE.getMessage()));
        Comment comment = Comment.builder()
                .member(writer)
                .issue(issue)
                .body(requestComment.commentBody())
                .build();
        commentRepository.save(comment);
        return new ResponseComment(comment);
    }

    @Override
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    public void deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public ResponseComment updateComment(UUID originalCommentId, RequestUpdateComment requestUpdateComment) {
        Comment comment = commentRepository.findById(originalCommentId)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));
        Member updater = memberRepository.findById(requestUpdateComment.getUpdaterUUID())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        validMemberUuid(comment, updater);
        Issue issue = issueRepository.findById(requestUpdateComment.getIssueId())
                .orElseThrow(() -> new IssueNotFoundException(ISSUE_NOT_FOUND, ISSUE_NOT_FOUND.getMessage()));
        comment.updateComment(requestUpdateComment, issue);
        return new ResponseComment(comment);
    }

    private static void validMemberUuid(final Comment comment, final Member updater) {
        if (!(comment.getMember().getId().equals(updater.getId()))) {
            throw new MismatchedMember(MISMATCHED_MEMBER, MISMATCHED_MEMBER.getMessage());
        }
    }
}