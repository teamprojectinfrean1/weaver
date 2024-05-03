package com.task.weaver.common.model;

import java.util.Locale;

/**
 * 프로젝트 권한 정보
 */
public enum Permission {
    LEADER, MEMBER
    ;

    public static Permission fromName(String type) {
        return Permission.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
