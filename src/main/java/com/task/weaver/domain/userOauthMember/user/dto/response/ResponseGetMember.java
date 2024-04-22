package com.task.weaver.domain.userOauthMember.user.dto.response;

import com.task.weaver.domain.member.dto.request.MemberDto;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseGetMember {

    private UUID userOauthUuid;
    private String nickname;
    private String id;
    private String email;
    private String password;
    private String profileImageUrl;

    private ResponseGetMember(User user){
        this.userOauthUuid = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.profileImageUrl = String.valueOf(user.getProfileImage());
    }

    private ResponseGetMember(OauthUser oauthMember) {
        this.nickname = oauthMember.getNickname();
        this.profileImageUrl = String.valueOf(oauthMember.getProfileImage());
    }

    private ResponseGetMember(MemberDto memberDto){
        this.userOauthUuid = memberDto.getUserId();
        this.id = memberDto.getId();
        this.nickname = memberDto.getNickname();
        this.email = memberDto.getEmail();
        this.password = memberDto.getPassword();
        this.profileImageUrl = String.valueOf(memberDto.getProfileImageUrl());
    }

    public static ResponseGetMember of(User user) {
        return new ResponseGetMember(user);
    }

    public static ResponseGetMember of(OauthUser oauthMember) {
        return new ResponseGetMember(oauthMember);
    }

    public static ResponseGetMember of(MemberDto memberDto) {
        return new ResponseGetMember(memberDto);
    }
}
