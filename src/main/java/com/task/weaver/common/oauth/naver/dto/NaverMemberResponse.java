package com.task.weaver.common.oauth.naver.dto;

import static com.task.weaver.common.model.OauthServerType.NAVER;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.oauth.entity.OauthId;
import com.task.weaver.domain.member.oauth.entity.OauthMember;

@JsonNaming(value = SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {

    public OauthMember toDomain() {
        return OauthMember.builder()
                .oauthId(new OauthId(String.valueOf(response.id), NAVER))
                .nickname(response.nickname)
                .profileImageUrl(response.profileImage)
                .loginType(LoginType.OAUTH)
                .build();
    }

    @JsonNaming(value = SnakeCaseStrategy.class)
    public record Response(
            String id,
            String nickname,
            String name,
            String email,
            String gender,
            String age,
            String birthday,
            String profileImage,
            String birthyear,
            String mobile
    ) {
    }
}
