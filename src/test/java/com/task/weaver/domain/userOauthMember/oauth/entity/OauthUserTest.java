package com.task.weaver.domain.userOauthMember.oauth.entity;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OauthUserTest {

    @InjectMocks
    private OauthUser oauthUser;

    @Test
    public void testUpdatePassword() {
        String newPassword = "newPassword123";
        assertThrows(UnsupportedOperationException.class, () -> oauthUser.updatePassword(newPassword));
    }

    @Test
    public void testUpdateNickname() {
        String newNickname = "newNickname";
        oauthUser.updateNickname(newNickname);
        assertEquals(newNickname, oauthUser.getNickname());
    }

    @Test
    public void testUpdateProfileImage() throws Exception {
        URL storedFileName = new URL("http://example.com/profile.jpg");
        oauthUser.updateProfileImage(storedFileName);
        assertEquals(storedFileName, oauthUser.getProfileImage());
    }

    @Test
    public void testUpdateMemberUuid() {
        UUID newMemberUuid = UUID.randomUUID();
        oauthUser.updateMemberUuid(newMemberUuid);
        assertEquals(newMemberUuid, oauthUser.getMemberUuid());
    }

    @Test
    public void testIsWeaver() {
        Assertions.assertFalse(oauthUser.isWeaver());
    }
}