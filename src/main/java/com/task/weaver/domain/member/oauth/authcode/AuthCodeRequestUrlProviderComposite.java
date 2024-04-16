package com.task.weaver.domain.member.oauth.authcode;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.member.UnsupportedPlatformsException;
import com.task.weaver.common.model.OauthServerType;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Composite 패턴 변형 <br/>
 * OauthServerType의 종류에 따라, 이에 해당하는 AuthCodeRequestUrlProvider를 사용하여 <br/>
 * URL을 생성할 수 있도록 한다.
 */
@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<OauthServerType, AuthCodeRequestUrlProvider> mapping;

    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers) {
        mapping = providers.stream()
                .collect(toMap(
                        AuthCodeRequestUrlProvider::supportServer,
                        identity()
                ));
    }

    public String provide(OauthServerType oauthServerType) {
        return getProvider(oauthServerType).provide();
    }

    private AuthCodeRequestUrlProvider getProvider(OauthServerType oauthServerType) {
        return Optional.ofNullable(mapping.get(oauthServerType))
                .orElseThrow(
                        () -> new UnsupportedPlatformsException(ErrorCode.UNSUPPORTED_OAUTH, "지원하지 않는 소셜 로그인 타입입니다."));
    }
}
