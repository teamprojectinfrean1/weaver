package com.task.weaver.domain.member;


import java.net.URL;
import java.util.UUID;

public interface UserOauthMember {

    boolean isWeaver();

    UUID getUuid();

    String getNickname();

    URL getProfileImage();

    void updateProfileImage(URL updatedImageUrlObject);

    void updateMemberUuid(UUID memberUuid);
}
