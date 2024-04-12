package com.task.weaver.common.oauth.google.dto;

import static com.task.weaver.common.model.OauthServerType.GOOGLE;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.task.weaver.domain.oauth.entity.OauthId;
import com.task.weaver.domain.oauth.entity.OauthMember;

@JsonNaming(value = SnakeCaseStrategy.class)
public record GoogleMemberResponse(
        String id,
        String name,
        String givenName,
        String picture,
        String locale
) {

    public OauthMember toDomain() {
        return OauthMember.builder()
                .oauthId(new OauthId(id, GOOGLE))
                .nickname(givenName)
                .profileImageUrl(picture)
                .build();
    }
}
