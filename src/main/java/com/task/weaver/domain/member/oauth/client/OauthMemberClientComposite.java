package com.task.weaver.domain.member.oauth.client;

import static java.util.stream.Collectors.toMap;
import static java.util.function.Function.identity;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.user.UnsupportedPlatformsException;
import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

@Component
public class OauthMemberClientComposite {

    /**
     * OauthMemberClient - Kakao .. Naver ..
     */
    private final Map<OauthServerType, OauthMemberClient> mapping;

    public OauthMemberClientComposite(Set<OauthMemberClient> clients) {
        mapping = clients.stream()
                .collect(toMap(
                        OauthMemberClient::supportServer,
                        identity()
                ));
    }

    /**
     * @param oauthServerType Kakao .. Naver ..
     * @param authCode 1차 요청으로 받은 Auth Code
     * @return OauthMember, mapping된 ApiClient를 통해 회원 가입 or 로그인 처리 후 반환
     */
    public OauthMember fetch(OauthServerType oauthServerType, String authCode) {
        return getClient(oauthServerType).fetch(authCode);
    }

    private OauthMemberClient getClient(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(
                        () -> new UnsupportedPlatformsException(ErrorCode.UNSUPPORTED_OAUTH, "지원하지 않는 소셜 로그인 타입입니다."));
    }
}
