package com.task.weaver.domain.userOauthMember;

import java.util.Locale;

public enum LoginType {

    WEAVER,
    OAUTH,
    ;

    public static LoginType fromName(String type) {
        return LoginType.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
