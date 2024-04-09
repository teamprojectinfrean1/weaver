package com.task.weaver.domain.member.user.dto.response;

import com.task.weaver.domain.member.user.entity.User;
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
public class ResponseGetUserList {
    private UUID userId;
    private String userNickname;
    private URL profileImage;

    public ResponseGetUserList(User user){
        this.userId = user.getUserId();
        this.userNickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }
}
