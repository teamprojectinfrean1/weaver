package com.task.weaver.domain.comment.service.impl;

import com.task.weaver.domain.comment.dto.request.RequestCreateComment;
import com.task.weaver.domain.comment.dto.response.ResponseComment;
import com.task.weaver.domain.comment.entity.Comment;
import com.task.weaver.domain.comment.repository.CommentRepository;
import com.task.weaver.domain.comment.service.CommentService;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.issue.repository.IssueRepository;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentServiceImplTest {
    private final CommentRepository commentRepository = mock(CommentRepository.class);
    private final IssueRepository issueRepository = mock(IssueRepository.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final CommentServiceImpl service = new CommentServiceImpl(commentRepository,memberRepository,issueRepository);
    private final Issue issue = mock(Issue.class);
    private final Member member = mock(Member.class);
    @Test
    public void IssueTest(){

    }
}