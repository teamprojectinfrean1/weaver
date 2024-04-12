package com.task.weaver.domain.user.dto.response;

import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;
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
    private URL profileImage;

    public ResponseGetUserForFront(User user){
        this.userId = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
    }
}
