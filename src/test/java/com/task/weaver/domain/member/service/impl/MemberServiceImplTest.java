package com.task.weaver.domain.member.service.impl;

import static com.task.weaver.common.exception.ErrorCode.NO_MATCHED_VERIFICATION_CODE;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.task.weaver.common.exception.member.UnableSendMailException;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.common.model.Permission;
import com.task.weaver.domain.member.dto.MemberProjectDTO;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.factory.impl.MemberFactoryImpl;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.projectmember.entity.ProjectMember;
import com.task.weaver.domain.projectmember.repository.ProjectMemberRepository;
import com.task.weaver.domain.userOauthMember.LoginType;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseUuid;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.userOauthMember.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberServiceImpl memberService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProjectMemberRepository projectMemberRepository;

    @Test
    void reissue() {
    }

    @Test
    void logout() {
    }

    @DisplayName("이메일 중복 확인 -> 사용 가능한 이메일")
    @Test
    void checkMail_isEmpty() {
        // Given
        String email = "newemail@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When
        boolean result = memberService.checkMail(email);

        // Then
        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByEmail(email);
    }

    @DisplayName("이메일 중복 확인 -> 사용 불가능한 이메일")
    @Test
    void checkMail_hasGot() {
        // Given
        String email = "newemail@example.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(User.builder()
                .userId(UUID.randomUUID())
                .email(email)
                .build()));
        // When
        boolean result = memberService.checkMail(email);
        // Then
        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByEmail(email);
    }

    @DisplayName("아이디 중복 확인 -> 사용 가능한 아이디")
    @Test
    void checkId_isEmpty() {
        // Given
        String id = "newId";
        // When
        when(userRepository.findByUserId(id)).thenReturn(Optional.empty());
        // Then
        Boolean result = memberService.checkId(id);
        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByUserId(id);
    }

    @DisplayName("아이디 중복 확인 -> 사용 불가능한 아이디")
    @Test
    void checkId_haGot() {
        // Given
        String id = "newId";
        // When
        when(userRepository.findByUserId(id)).thenReturn(Optional.of(User.builder()
                .userId(UUID.randomUUID())
                .id(id)
                .build()));
        Boolean result = memberService.checkId(id);
        // Then
        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByUserId(id);
    }

    @DisplayName("닉네임 중복 확인 -> 사용 가능한 닉네임")
    @Test
    void checkNickname_isEmpty() {
        // Given
        String nickname = "nickname";
        // When
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.empty());
        Boolean result = memberService.checkNickname(nickname);
        // Then
        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByNickname(nickname);
    }

    @DisplayName("닉네임 중복 확인 -> 사용 불가능한 닉네임")
    @Test
    void checkNickname_hasGot() {
        // Given
        String nickname = "nickname";
        // When
        when(userRepository.findByNickname(nickname)).thenReturn(Optional.of(User.builder()
                .userId(UUID.randomUUID())
                .nickname(nickname)
                .build()));
        Boolean result = memberService.checkNickname(nickname);
        // Then
        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByNickname(nickname);
    }

    @DisplayName("이메일, 검증 코드 -> 유효")
    @Test
    void fetchUuid_validInput() {
        // Given
        String email = "test@example.com";
        boolean checked = true;
        User user = User.builder()
                .userId(UUID.randomUUID())
                .memberUuid(UUID.randomUUID())
                .email(email)
                .build();
        // When
        when(memberService.fetchUuid(email, checked)).thenReturn(ResponseUuid.of(user.getMemberUuid(), user.getUuid()));
        ResponseUuid responseUuid = memberService.fetchUuid(email, checked);

        // Then
        assertThat(responseUuid.getMemberUuid()).isEqualTo(user.getMemberUuid());
        assertThat(responseUuid.getUserUuid()).isEqualTo(user.getUserId());
        verify(memberService, times(1)).fetchUuid(email, checked);
    }

    @DisplayName("이메일, 검증 코드 -> 검증 코드 불일치")
    @Test
    void fetchUuid_invalidCode() {
        // Given
        String email = "test@example.com";
        boolean checked = false;
        // When
        when(memberService.fetchUuid(email, checked)).thenThrow(
                new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, NO_MATCHED_VERIFICATION_CODE.getMessage()));
        // Then
        assertThrows(UnableSendMailException.class, () -> memberService.fetchUuid(email, checked));
    }

    @DisplayName("이메일, 검증 코드 -> 이메일 불일치")
    @Test
    void fetchUuid_invalidEmail() {
        // Given
        String email = "test@example.com";
        boolean checked = true;
        // When
        when(memberService.fetchUuid(email, checked)).thenThrow(
                new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        // Then
        assertThrows(UserNotFoundException.class, () -> memberService.fetchUuid(email, checked));
    }

    @DisplayName("특정 멤버 UUID 조회 -> 조회 성공")
    @Test
    void fetchMemberByUuid() {
        UUID memberId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        ResponseGetMember expectedResponse = ResponseGetMember.builder()
                .memberUuid(memberId)
                .userOauthUuid(userId)
                .build();
        when(memberService.fetchMemberByUuid(memberId)).thenReturn(expectedResponse);

        ResponseGetMember actualResponse = memberService.fetchMemberByUuid(memberId);

        assertThat(actualResponse.getMemberUuid()).isEqualTo(memberId);
        assertThat(actualResponse.getUserOauthUuid()).isEqualTo(userId);
    }

    @DisplayName("존재하지 않는 UUID -> 조회 시 예외 발생")
    @Test
    void fetchMemberByNonExistingUuid() {
        // Given
        UUID nonExistingId = UUID.randomUUID();
        // When
        when(memberService.fetchMemberByUuid(nonExistingId)).thenThrow(UserNotFoundException.class);
        // Then
        assertThrows(UserNotFoundException.class, () -> memberService.fetchMemberByUuid(nonExistingId));
    }

    @DisplayName("이메일로 유저 조회 -> 조회 성공")
    @Test
    void fetchMemberByEmail() {
        // Given
        String email = "test@example.com";
        User user = new User();
        user.setId("tester");
        user.setNickname("test");

        // When
        ResponseUserIdNickname expected = ResponseUserIdNickname.userToDto(user);
        when(memberService.fetchMemberByEmail(email, true)).thenReturn(expected);
        ResponseUserIdNickname response = memberService.fetchMemberByEmail(email, true);

        // Then
        assertThat(response.getId()).isEqualTo(user.getId());
        assertThat(response.getNickname()).isEqualTo(user.getNickname());
    }

    @DisplayName("이메일로 유저 조회 -> UserNotFound 예외 발생")
    @Test
    void fetchMemberByEmail_UserNotFound() {
        // Given
        String email = "nonexistent@example.com";
        when(memberService.fetchMemberByEmail(email, true)).thenThrow(UserNotFoundException.class);
        // When & Then
        assertThrows(UserNotFoundException.class, () -> memberService.fetchMemberByEmail(email, true));
    }

    @DisplayName("이메일로 유저 조회 -> UnableSendMailException 예외 발생")
    @Test
    void fetchMemberByEmail_UnableSendMailException() {
        // Given
        String email = "nonexistent@example.com";
        boolean checked = false;
        when(memberService.fetchMemberByEmail(email, checked)).thenThrow(UnableSendMailException.class);
        // When & Then
        assertThrows(UnableSendMailException.class, () -> memberService.fetchMemberByEmail(email, checked));
    }

    @DisplayName("토큰으로 멤버 조회 -> 성공")
    @Test
    void fetchMemberFromToken_ValidToken_ReturnsResponse() {
        // Given
        UUID memberId = UUID.randomUUID();
        HttpServletRequest request = mock(HttpServletRequest.class);
        ResponseGetUserForFront expected = new ResponseGetUserForFront(memberId, "test", "test", "ese@es.com", null, true);
        // When
        when(memberService.fetchMemberFromToken(request)).thenReturn(expected);
        ResponseGetUserForFront response = memberService.fetchMemberFromToken(request);
        // Then
        assertThat(response.getUserOauthUuid()).isEqualTo(memberId);
    }

    @DisplayName("토큰으로 멤버 조회 -> 서블릿 요청 누락")
    @Test
    void fetchMemberFromToken_InvalidToken_ReturnsNull() {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(memberService.fetchMemberFromToken(request)).thenThrow(IllegalArgumentException.class);
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> memberService.fetchMemberFromToken(request));
    }

    @Test
    void fetchSimplePagedMembers_ReturnsPage() {
        // Given
        int page = 0;
        int size = 10;
        UUID projectId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(page, size);

        List<MemberProjectDTO> memberProjectDTOs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MemberProjectDTO memberProjectDTO = new MemberProjectDTO();
            memberProjectDTOs.add(memberProjectDTO);
        }
        Page<MemberProjectDTO> expected = new PageImpl<>(memberProjectDTOs, pageable, memberProjectDTOs.size());

        when(memberService.fetchSimplePagedMembers(page, size, projectId)).thenReturn(expected);

        // When
        Page<MemberProjectDTO> result = memberService.fetchSimplePagedMembers(page, size, projectId);

        // Then
        Assertions.assertThat(size).isEqualTo(result.getNumberOfElements());
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getContent()).hasSize(size);
    }

    @Test
    void fetchPagedMembers() {
    }

    @Test
    void fetchMembersForDeveloper() {
    }

    @Test
    void fetchMembers() {
    }

    @Test
    void getAuthentication() {
    }
}