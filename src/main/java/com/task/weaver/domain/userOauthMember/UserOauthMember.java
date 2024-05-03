package com.task.weaver.domain.userOauthMember;


import java.net.URL;
import java.util.UUID;

public interface UserOauthMember {

    boolean isWeaver();

    UUID getUuid();

    UUID getMemberUuid();

    String getNickname();

    URL getProfileImage();

    String getPassword();

    void updateProfileImage(URL updatedImageUrlObject);

    void updateMemberUuid(UUID memberUuid);

    void updatePassword(String updatePassword);

    void updateNickname(final String nickname);
}
