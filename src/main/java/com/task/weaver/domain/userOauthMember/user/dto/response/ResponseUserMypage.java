package com.task.weaver.domain.userOauthMember.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUserMypage {
    private String nickname;
    private String email;
    private String profileImageUrl;
}
