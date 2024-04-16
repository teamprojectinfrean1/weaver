package com.task.weaver.common.oauth.google.dto;

import static com.task.weaver.common.model.OauthServerType.GOOGLE;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.task.weaver.domain.member.oauth.entity.OauthId;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import java.net.URL;

@JsonNaming(value = SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String name,
        String givenName,
        URL picture,
        String locale
) {

    public OauthUser toDomain() {
        return OauthUser.builder()
                .oauthId(new OauthId(id, GOOGLE))
                .nickname(givenName)
                .profileImageUrl(picture)
                .build();
    }
}
