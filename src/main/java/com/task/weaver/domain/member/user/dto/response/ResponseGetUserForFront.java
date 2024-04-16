package com.task.weaver.domain.member.user.dto.response;

import com.task.weaver.domain.member.oauth.entity.OauthUser;
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
    private boolean isWeaver;

    private ResponseGetUserForFront(User user){
        this.userId = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImage = String.valueOf(user.getProfileImage());
        this.isWeaver = user.isWeaver();
    }

    private ResponseGetUserForFront(OauthUser oauthMember){
        this.userId = oauthMember.getUuid();
        this.id = null;
        this.nickname = oauthMember.getNickname();
        this.email = null;
        this.profileImage = String.valueOf(oauthMember.getProfileImage());
        this.isWeaver = oauthMember.isWeaver();
    }

    public static ResponseGetUserForFront of(final User user) {
        return new ResponseGetUserForFront(user);
    }

    public static ResponseGetUserForFront of(final OauthUser oauthMember) {
        return new ResponseGetUserForFront(oauthMember);
    }
}
