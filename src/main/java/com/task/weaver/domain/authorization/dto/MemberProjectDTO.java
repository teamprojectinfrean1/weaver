package com.task.weaver.domain.authorization.dto;

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
    private UUID oauthUserId;
    private String nickname;
    private String userProfileImage;
    private String oauthProfileImage;
}
