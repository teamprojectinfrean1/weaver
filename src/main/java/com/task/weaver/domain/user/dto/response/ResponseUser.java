package com.task.weaver.domain.user.dto.response;

import com.task.weaver.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseUser {
    private UUID userId;
    private String id;
    private String userNickname;
    private String email;
    private String password;
    private String profileImage;

    public ResponseUser(User user){
        this.userId = user.getUserId();
        this.id = user.getId();
        this.userNickname = user.getNickname();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
