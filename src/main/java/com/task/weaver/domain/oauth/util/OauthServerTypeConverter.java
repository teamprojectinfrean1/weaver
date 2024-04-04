package com.task.weaver.domain.oauth.util;

import com.task.weaver.common.model.OauthServerType;
import org.springframework.core.convert.converter.Converter;

public class OauthServerTypeConverter implements Converter<String, OauthServerType> {

    @Override
    public OauthServerType convert(final String source) {
        return OauthServerType.fromName(source);
    }
}
