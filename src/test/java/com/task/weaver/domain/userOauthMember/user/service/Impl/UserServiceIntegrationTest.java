package com.task.weaver.domain.userOauthMember.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.common.exception.member.DuplicateEmailException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.member.dto.request.RequestSignIn;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.factory.MemberFactory;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.member.service.MemberService;
import com.task.weaver.domain.member.service.impl.MemberServiceImpl;
import com.task.weaver.domain.userOauthMember.LoginType;
import com.task.weaver.domain.userOauthMember.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.userOauthMember.user.repository.UserRepository;
import com.task.weaver.domain.userOauthMember.user.service.UserService;
import com.task.weaver.domain.userOauthMember.util.MemberStorageHandler;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    private MemberRepository memberRepository;

    @Mock
    private MemberFactory memberFactory;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @DisplayName(value = "유저 로그인 실패 (해당 유저가 존재하지 않음)")
    @Test
    public void testWeaverLogin_UserNotFound() {
        RequestSignIn requestSignIn = new RequestSignIn("testId", "testPassword");
        when(userRepository.findByUserId(requestSignIn.id())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.weaverLogin(requestSignIn));
    }

    @DisplayName("유저 로그인 실패 (패스워드 불일치)")
    @Test
    public void testWeaverLogin_InvalidPassword() {
        RequestSignIn requestSignIn = new RequestSignIn("testId", "testPassword");
        User user = new User(UUID.randomUUID(), UUID.randomUUID(), "testId", "testNickname", "testEmail", "wrongPassword", null);
        when(userRepository.findByUserId(requestSignIn.id())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> userService.weaverLogin(requestSignIn));
    }

    @DisplayName("Weaver 회원 가입 성공")
    @Test
    public void testWeaverRegister_Successfully() throws IOException {
       //GIVEN
        RequestCreateUser requestCreateUser = new RequestCreateUser("testId", "testName", "testEmail@example.com",
                "testPassword");
        MultipartFile multipartFile = mock(MultipartFile.class);

        //WHEN
        ResponseGetMember responseGetMember = userService.addUser(requestCreateUser, null);

       //THEN
        Assertions.assertThat(responseGetMember).isNotNull();
    }

    @DisplayName("유저 로그인 성공")
    @Test
    public void testWeaverLogin_Successfully() {
        UserRepository userRepository = mock(UserRepository.class);
        MemberRepository memberRepository = mock(MemberRepository.class);
        MemberServiceImpl memberService = mock(MemberServiceImpl.class);
        UserServiceImpl userService = mock(UserServiceImpl.class);

        RequestSignIn requestSignIn = new RequestSignIn("testId", "testPassword");
        User user = new User(UUID.randomUUID(), UUID.randomUUID(), "testId", "testNickname", "testEmail", "testPassword", null);

        // userRepository.findByUserId() 호출 시 존재하는 유저 반환
        when(userRepository.save(user)).thenReturn(user);
        when(userRepository.findByUserId(requestSignIn.id())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(requestSignIn.password(), user.getPassword())).thenReturn(true);

        // memberRepository.findByUser() 호출 시 적절한 반환값 설정
        Member member = new Member(UUID.randomUUID(), user, null, LoginType.WEAVER, true, null, null, null, null, null);
        when(memberRepository.save(member)).thenReturn(member);
        when(memberRepository.findByUser(user)).thenReturn(Optional.of(member));

        // memberService.getAuthentication() 호출 시 적절한 반환값 설정
        ResponseToken expectedToken = new ResponseToken("access_token", "refresh_token");
        when(memberService.getAuthentication(any())).thenReturn(expectedToken);

        ResponseToken responseToken = userService.weaverLogin(requestSignIn);

        Assertions.assertThat(responseToken).isNotNull();
    }


}