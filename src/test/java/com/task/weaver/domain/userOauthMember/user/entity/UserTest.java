package com.task.weaver.domain.userOauthMember.user.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @InjectMocks
    private User user;

    @Test
    void testUpdateEmail() {
        String newEmail = "newEmail@example.com";
        user.updateEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    public void testUpdatePassword() {
        String newPassword = "newPassword123";
        user.updatePassword(newPassword);
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    public void testUpdateNickname() {
        String newNickname = "newNickname";
        user.updateNickname(newNickname);
        assertEquals(newNickname, user.getNickname());
    }

    @Test
    public void testUpdateProfileImage() throws Exception {
        URL storedFileName = new URL("http://example.com/profile.jpg");
        user.updateProfileImage(storedFileName);
        assertEquals(storedFileName, user.getProfileImage());
    }

    @Test
    public void testUpdateMemberUuid() {
        UUID newMemberUuid = UUID.randomUUID();
        user.updateMemberUuid(newMemberUuid);
        assertEquals(newMemberUuid, user.getMemberUuid());
    }

    @Test
    public void testIsWeaver() {
        assertTrue(user.isWeaver());
    }
}