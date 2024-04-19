package com.task.weaver.domain.authorization.dto.request;

import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.oauth.entity.OauthId;
import com.task.weaver.domain.member.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.member.user.entity.User;
import java.net.URL;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDto {

    private boolean isWeaver;
    private UUID userId;
    private String nickname;
    private String id;
    private String email;
    private String password;
    private URL profileImageUrl;

    public static MemberDto memberDomainToDto(UserOauthMember userOauthMember, Member member) {
        return MemberDto.builder()
                .isWeaver(userOauthMember.isWeaver())
                .userId(userOauthMember.getUuid())
                .nickname(userOauthMember.getNickname())
                .profileImageUrl(userOauthMember.getProfileImage())
                .id(userOauthMember.isWeaver() ? member.getUser().getId() : null)
                .email(userOauthMember.isWeaver() ? member.getUser().getEmail() : null)
                .password(userOauthMember.isWeaver() ? member.getUser().getPassword() : null)
                .build();
    }
}
