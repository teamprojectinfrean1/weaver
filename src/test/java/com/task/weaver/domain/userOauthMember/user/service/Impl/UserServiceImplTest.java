package com.task.weaver.domain.userOauthMember.user.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.common.exception.member.DuplicateEmailException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.member.dto.request.RequestSignIn;
import com.task.weaver.domain.member.factory.MemberFactory;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.member.service.MemberService;
import com.task.weaver.domain.userOauthMember.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.userOauthMember.user.repository.UserRepository;
import com.task.weaver.domain.userOauthMember.util.MemberStorageHandler;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3Uploader s3Uploader;

    @Mock
    private MemberService memberService;

    @Mock
    private MemberFactory memberFactory;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private OauthMemberRepository oauthMemberRepository;

    @Mock
    private MemberStorageHandler memberStorageHandler;

    private MockMvc mockMvc;

    @Test
    public void testWeaverLogin_UserNotFound() {
        RequestSignIn requestSignIn = new RequestSignIn("testId", "testPassword");
        when(userRepository.findByUserId(requestSignIn.id())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.weaverLogin(requestSignIn));
    }

    @Test
    public void testWeaverLogin_InvalidPassword() {
        RequestSignIn requestSignIn = new RequestSignIn("testId", "testPassword");
        User user = new User(UUID.randomUUID(), UUID.randomUUID(), "testId", "testNickname", "testEmail", "wrongPassword", null);
        when(userRepository.findByUserId(requestSignIn.id())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.weaverLogin(requestSignIn));
    }

    @Test
    public void testWeaverRegister_Duplicate_Email_ThrowsException() throws IOException {
        // Arrange
        RequestCreateUser requestCreateUser = mock(RequestCreateUser.class);
        MultipartFile profileImage = mock(MultipartFile.class);

        Mockito.doReturn(requestCreateUser.dtoToUserDomain(passwordEncoder))
                .when(userService)
                .addUser(requestCreateUser, profileImage);


        // Act & Assert
        assertThrows(DuplicateEmailException.class, () -> userService.addUser(requestCreateUser, profileImage));
    }

//    @Test
//    public void testWeaverRegister_Success() {
//        // Arrange
//        RequestCreateUser requestCreateUser = mock(RequestCreateUser.class);
//        MultipartFile profileImage = mock(MultipartFile.class);
//
//        // 가짜 데이터 설정
//        when(requestCreateUser.getName()).thenReturn("John Doe");
//        when(requestCreateUser.getEmail()).thenReturn("johndoe@example.com");
//        when(profileImage.getOriginalFilename()).thenReturn("profile.jpg");
//
//        // 이메일 중복 없는 경우 정상 동작 시나리오 설정
//        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
//        when(s3Uploader.uploadFile(any(MultipartFile.class), anyString())).thenReturn("https://example.com/profile.jpg");
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
//
//        // Act
//        User createdUser = userCreationService.addUser(requestCreateUser, profileImage);
//
//        // Assert
//        assertNotNull(createdUser);
//        assertEquals("John Doe", createdUser.getName());
//        assertEquals("johndoe@example.com", createdUser.getEmail());
//        assertEquals("https://example.com/profile.jpg", createdUser.getProfileImageUrl());
//        assertEquals("encodedPassword", createdUser.getPassword());
//    }

//    @Test
//    public void testIsExistEmail_WhenEmailExists_ThrowsException() {
//        String existingEmail = "existing@example.com";
//        when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(new User()));
//
//        assertThrows(RuntimeException.class, () -> userService.isExistEmail(existingEmail));
//    }
//
//    @Test
//    public void testIsExistEmail_WhenEmailDoesNotExist_NoExceptionThrown() {
//        String newEmail = "new@example.com";
//        when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());
//
//        assertDoesNotThrow(() -> userService.isExistEmail(newEmail));
//    }

    @Test
    void addUser() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void updateProfile() {
    }

    @Test
    void testUpdateUser() {
    }

    @Test
    void deleteUser() {
    }
}