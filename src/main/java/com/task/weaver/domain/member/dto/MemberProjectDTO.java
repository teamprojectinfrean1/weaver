package com.task.weaver.domain.member.dto;

import com.task.weaver.common.model.Permission;
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
    private String userNickname;
    private String userProfileImage;
    private Permission permission;
    private boolean hasAssigneeIssueInProgress;

    public MemberProjectDTO(UserOauthMember userOauthMember, boolean assigneeIssue, Permission permission) {
        this.memberId = userOauthMember.getMemberUuid();
        this.userId = userOauthMember.getUuid();
        this.userNickname = userOauthMember.getNickname();
        this.userProfileImage = String.valueOf(userOauthMember.getProfileImage());
        this.permission = permission;
        this.hasAssigneeIssueInProgress = assigneeIssue;
    }
}
