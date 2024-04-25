package com.task.weaver.domain.member.dto;

import com.task.weaver.domain.userOauthMember.UserOauthMember;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberProjectDTO {
    private UUID memberId;
    private UUID userId;
    private String nickname;
    private String userProfileImage;
    private boolean hasAssigneeIssueInProgress;

    public MemberProjectDTO(UserOauthMember userOauthMember, boolean assigneeIssue) {
        this.memberId = userOauthMember.getMemberUuid();
        this.userId = userOauthMember.getUuid();
        this.nickname = userOauthMember.getNickname();
        this.userProfileImage = String.valueOf(userOauthMember.getProfileImage());
        this.hasAssigneeIssueInProgress = assigneeIssue;
    }
}
