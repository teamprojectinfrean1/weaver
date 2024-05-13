package com.task.weaver.domain.comment.service.impl;

import static com.task.weaver.common.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.ISSUE_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.MISMATCHED_MEMBER;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.comment.CommentNotFoundException;
import com.task.weaver.common.exception.issue.IssueNotFoundException;
import com.task.weaver.common.exception.member.MismatchedMember;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.request.RequestUpdateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.dto.response.ResponseCommentList;
import com.task.weaver.domain.comment.dto.response.ResponseCommentUuid;
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
    public ResponseComment fetchComment(UUID uuid) {
        return new ResponseComment(getComment(uuid));
    }

    @Override
    public ResponsePageComment<ResponseCommentList, Comment> getComments(int page, int size, UUID issueId) {
        Pageable pageable = getPageable(Sort.by("regDate").descending(), page, size);
        Page<Comment> comments = commentRepository.findByIssue(getIssue(issueId), pageable);
        Function<Comment, ResponseCommentList> fn = ResponseCommentList::new;
        return new ResponsePageComment<>(comments, fn);
    }

    private Pageable getPageable(Sort sort, int page, int size) {
        return PageRequest.of(page - 1, size, sort);
    }

    @Override
    @Transactional
    public ResponseComment addComment(RequestCreateComment requestComment) {
        Comment comment = Comment.builder()
                .member(getMember(requestComment.writerId()))
                .issue(getIssue(requestComment.issueId()))
                .body(requestComment.commentBody())
                .build();
        commentRepository.save(comment);
        return new ResponseComment(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Override
    @Transactional
    public ResponseCommentUuid deleteComment(UUID commentId) {
        commentRepository.deleteById(commentId);
        return new ResponseCommentUuid(commentId);
    }

    @Override
    @Transactional
    public ResponseComment updateComment(UUID originalCommentId, RequestUpdateComment requestUpdateComment) {
        Comment comment = getComment(originalCommentId);
        Member updater = getMember(requestUpdateComment.getWriterId());
        validMemberUuid(comment, updater);
        Issue issue = getIssue(requestUpdateComment.getIssueId());
        comment.updateComment(requestUpdateComment, issue);
        return new ResponseComment(comment);
    }

    private static void validMemberUuid(final Comment comment, final Member updater) {
        if (!(comment.getMember().getId().equals(updater.getId()))) {
            throw new MismatchedMember(MISMATCHED_MEMBER, MISMATCHED_MEMBER.getMessage());
        }
    }

    private Comment getComment(final UUID uuid) {
        return commentRepository.findById(uuid)
                .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND, COMMENT_NOT_FOUND.getMessage()));
    }

    private Issue getIssue(final UUID issueId) {
        return issueRepository.findById(issueId)
                .orElseThrow(() -> new IssueNotFoundException(ISSUE_NOT_FOUND, ISSUE_NOT_FOUND.getMessage()));
    }

    private Member getMember(final UUID requestComment) {
        return memberRepository.findById(requestComment)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
    }
}