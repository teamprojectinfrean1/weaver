package com.task.weaver.domain.oauth.client;

import static java.util.stream.Collectors.toMap;
import static java.util.function.Function.identity;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.user.UnsupportedPlatformsException;
import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.oauth.entity.OauthMember;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OauthMemberClientComposite {

    private final Map<OauthServerType, OauthMemberClient> mapping;

    public OauthMemberClientComposite(Set<OauthMemberClient> clients) {
        mapping = clients.stream()
                .collect(toMap(
                        OauthMemberClient::supportServer,
                        identity()
                ));
    }

    public OauthMember fetch(OauthServerType oauthServerType, String authCode) {
        return getClient(oauthServerType).fetch(authCode);
    }

    private OauthMemberClient getClient(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(
                        () -> new UnsupportedPlatformsException(ErrorCode.UNSUPPORTED_OAUTH, "지원하지 않는 소셜 로그인 타입입니다."));
    }
}
