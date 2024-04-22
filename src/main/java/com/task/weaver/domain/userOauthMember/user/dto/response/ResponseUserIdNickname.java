package com.task.weaver.domain.userOauthMember.user.dto.response;

import com.task.weaver.domain.userOauthMember.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ResponseUserIdNickname {

    private String id;
    private String nickname;

    public static ResponseUserIdNickname userToDto(User user) {
        return ResponseUserIdNickname.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
    }
}
