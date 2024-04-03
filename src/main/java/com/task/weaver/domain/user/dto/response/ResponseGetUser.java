package com.task.weaver.domain.user.dto.response;

import com.task.weaver.domain.user.entity.User;
import java.net.URL;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseGetUser {
    private UUID userId;
    private String id;
    private String nickname;
    private String email;
    private String password;
    private URL profileImage;

    public ResponseGetUser(User user){
        this.userId = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.profileImage = user.getProfileImage();
    }
}
