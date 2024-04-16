package com.task.weaver.common.oauth.naver.dto;

import static com.task.weaver.common.model.OauthServerType.NAVER;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.task.weaver.domain.member.oauth.entity.OauthId;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import java.net.URL;

@JsonNaming(value = SnakeCaseStrategy.class)
public record NaverMemberResponse(
        String resultcode,
        String message,
        Response response
) {

    public OauthUser toDomain() {
        return OauthUser.builder()
                .oauthId(new OauthId(String.valueOf(response.id), NAVER))
                .nickname(response.nickname)
                .profileImageUrl(response.profileImage)
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
            URL profileImage,
            String birthyear,
            String mobile
    ) {
    }
}
