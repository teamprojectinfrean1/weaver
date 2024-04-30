package com.task.weaver.domain.member.dto.response;

import com.task.weaver.domain.issue.dto.response.GetIssueListResponse;
import com.task.weaver.domain.issue.entity.Issue;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import java.net.URL;
import java.util.UUID;
import lombok.Builder;

@Builder
public record GetMemberListResponse (UUID memberId,
                                     UUID userId,
                                     String userNickname,
                                     URL userProfileImage,
                                     boolean hasAssigneeIssueInProgress){

    public static GetMemberListResponse of(Member member) {
        UserOauthMember userOauthMember = member.resolveMemberByLoginType();
        boolean inProgress = member.hasAssigneeIssueInProgress();
        return GetMemberListResponse.builder()
                .memberId(userOauthMember.getMemberUuid())
                .userId(userOauthMember.getUuid())
                .userNickname(userOauthMember.getNickname())
                .userProfileImage(userOauthMember.getProfileImage())
                .hasAssigneeIssueInProgress(inProgress)
                .build();
    }
}
