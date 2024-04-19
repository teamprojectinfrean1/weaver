package com.task.weaver.domain.member.user.dto.response;

import com.task.weaver.domain.authorization.dto.request.MemberDto;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
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
public class ResponseGetUserForFront {
    private UUID userOauthUuid;
    private String id;
    private String nickname;
    private String email;
    private URL profileImage;
    private boolean isWeaver;

    private ResponseGetUserForFront(User user){
        this.userOauthUuid = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.isWeaver = user.isWeaver();
    }

    private ResponseGetUserForFront(OauthUser oauthMember){
        this.userOauthUuid = oauthMember.getUuid();
        this.id = null;
        this.nickname = oauthMember.getNickname();
        this.email = null;
        this.profileImage = oauthMember.getProfileImage();
        this.isWeaver = oauthMember.isWeaver();
    }

    private ResponseGetUserForFront(MemberDto memberDto){
        this.userOauthUuid = memberDto.getUserId();
        this.id = memberDto.getId();
        this.nickname = memberDto.getNickname();
        this.email = memberDto.getEmail();
        this.profileImage = memberDto.getProfileImageUrl();
        this.isWeaver = memberDto.isWeaver();
    }

    public static ResponseGetUserForFront of(final User user) {
        return new ResponseGetUserForFront(user);
    }

    public static ResponseGetUserForFront of(final OauthUser oauthMember) {
        return new ResponseGetUserForFront(oauthMember);
    }

    public static ResponseGetUserForFront of(final MemberDto memberDto) {
        return new ResponseGetUserForFront(memberDto);
    }
}
