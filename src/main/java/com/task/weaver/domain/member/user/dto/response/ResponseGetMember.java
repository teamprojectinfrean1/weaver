package com.task.weaver.domain.member.user.dto.response;

import com.task.weaver.domain.member.oauth.entity.OauthId;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.domain.member.user.entity.User;
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

    private UUID userId;
    private UUID oauthId;
    private String nickname;
    private String id;
    private String email;
    private String password;
    private String profileImageUrl;
    private OauthId oauthServerId;

    private ResponseGetMember(User user){
        this.userId = user.getUserId();
        this.id = user.getId();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.profileImageUrl = String.valueOf(user.getProfileImage());
    }

    private ResponseGetMember(OauthUser oauthMember) {
        this.oauthId = oauthMember.getUuid();
        this.oauthServerId = oauthMember.oauthId();
        this.nickname = oauthMember.getNickname();
        this.profileImageUrl = String.valueOf(oauthMember.getProfileImage());
    }

    public static ResponseGetMember of(User user) {
        return new ResponseGetMember(user);
    }

    public static ResponseGetMember of(OauthUser oauthMember) {
        return new ResponseGetMember(oauthMember);
    }
}
