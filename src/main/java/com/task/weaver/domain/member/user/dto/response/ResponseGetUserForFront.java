package com.task.weaver.domain.member.user.dto.response;

import com.task.weaver.domain.member.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseGetUserForFront {
    private UUID userId;
    private String id;
    private String nickname;
    private String email;
    private String profileImage;

    public ResponseGetUserForFront(User user){
        this.userId = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}
