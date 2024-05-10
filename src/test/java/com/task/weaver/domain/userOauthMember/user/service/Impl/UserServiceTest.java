package com.task.weaver.domain.userOauthMember.user.service.Impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.domain.member.dto.request.RequestSignIn;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.userOauthMember.user.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName(value = "유저 로그인 실패 (해당 유저가 존재하지 않음)")
    @Test
    public void testWeaverLogin_UserNotFound() {
        // Given
        RequestSignIn requestSignIn = new RequestSignIn("testId", "testPassword");

        // Mock UserRepository behavior
        when(userRepository.findByUserId(requestSignIn.id())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.weaverLogin(requestSignIn));

        // Verify
        verify(userRepository, times(1)).findByUserId(requestSignIn.id());
        verifyNoInteractions(jwtTokenProvider);
    }

    @DisplayName("유저 로그인 실패 (패스워드 불일치)")
    @Test
    public void testWeaverLogin_InvalidPassword() {
        // Given
        RequestSignIn requestSignIn = new RequestSignIn("testId", "testPassword");

        User user = new User(UUID.randomUUID(), UUID.randomUUID(), "testId", "testNickname", "testEmail",
                "wrongPassword", null);
        when(userRepository.findByUserId(requestSignIn.id())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestSignIn.password(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.weaverLogin(requestSignIn));

        // Verify
        verify(userRepository, times(1)).findByUserId(requestSignIn.id());
        verify(passwordEncoder, times(1)).matches(requestSignIn.password(), user.getPassword());
        verifyNoInteractions(jwtTokenProvider);
    }
}