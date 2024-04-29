package com.task.weaver.common.model;

import java.util.Locale;

public enum ProjectPermission {
    TEAM_LEADER, TEAM_MEMBER
    ;

    public static ProjectPermission fromName(String type) {
        return ProjectPermission.valueOf(type.toUpperCase(Locale.ENGLISH));
    }
}
