package com.task.weaver.domain.userOauthMember.oauth.util;

import com.task.weaver.common.model.OauthServerType;
import org.springframework.core.convert.converter.Converter;

public class OauthServerTypeConverter implements Converter<String, OauthServerType> {

    /**
     * @Configuration WebConfigurer 설정 필요
     * @param source the source object to convert, which must be an instance of {@code S} (never {@code null}) <br/>
     * @return String source -> OauthServerType 변환
     */
    @Override
    public OauthServerType convert(final String source) {
        return OauthServerType.fromName(source);
    }
}
