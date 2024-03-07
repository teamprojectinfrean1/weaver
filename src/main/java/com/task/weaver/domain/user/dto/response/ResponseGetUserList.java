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
public class ResponseGetUserList {
    private UUID userId;
    private String userNickname;
    private String profileImage;

    public ResponseGetUserList(User user){
        this.userId = user.getUserId();
        this.userNickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
