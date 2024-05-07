package com.task.weaver.domain.userOauthMember.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.EMAIL_ALREADY_EXISTS;
import static com.task.weaver.common.exception.ErrorCode.INVALID_PASSWORD;
import static com.task.weaver.common.exception.ErrorCode.MISMATCHED_INPUT_VALUE;
import static com.task.weaver.common.exception.ErrorCode.MISMATCHED_PASSWORD;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.common.exception.member.DuplicateEmailException;
import com.task.weaver.common.exception.member.MismatchedInputException;
import com.task.weaver.common.exception.member.MismatchedPassword;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.member.dto.request.RequestSignIn;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.member.entity.Member;
import com.task.weaver.domain.member.factory.MemberFactory;
import com.task.weaver.domain.member.repository.MemberRepository;
import com.task.weaver.domain.member.service.MemberService;
import com.task.weaver.domain.userOauthMember.UserOauthMember;
import com.task.weaver.domain.userOauthMember.oauth.entity.OauthUser;
import com.task.weaver.domain.userOauthMember.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseSimpleURL;
import com.task.weaver.domain.userOauthMember.user.entity.User;
import com.task.weaver.domain.userOauthMember.user.repository.UserRepository;
import com.task.weaver.domain.userOauthMember.user.service.UserService;
import com.task.weaver.domain.userOauthMember.util.MemberStorageHandler;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String DIR_NAME = "images";
    private static final String EMAIL = "email";
    private static final String NICKNAME = "nickname";
    private static final String PASSWORD = "password";
    private static final String CURRENT_PASSWORD = "currentPassword";
    private static final String UPDATE_PASSWORD = "updatePassword";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;
    private final MemberService memberService;
    private final MemberFactory memberFactory;
    private final MemberRepository memberRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final MemberStorageHandler memberStorageHandler;

    @Override
    @Transactional(readOnly = true)
    public ResponseToken weaverLogin(RequestSignIn requestSignIn) {
        return userRepository.findByUserId(requestSignIn.id())
                .flatMap(user -> getResponseToken(requestSignIn, user))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
    }

    private Optional<ResponseToken> getResponseToken(final RequestSignIn requestSignIn, final User user) {
        hasMatched(user, requestSignIn.password());
        return memberRepository.findByUser(user)
                .map(memberService::getAuthentication);
    }

    private void hasMatched(final User user, final String requestPassword) {
        if (!checkPassword(user, requestPassword)) {
            throw new InvalidPasswordException(INVALID_PASSWORD, INVALID_PASSWORD.getMessage());
        }
    }

    private boolean checkPassword(User user, String requestPassword) {
        return passwordEncoder.matches(requestPassword, user.getPassword()); // 암호화된 비밀번호가 뒤로 와야 한다 순서
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseGetMember addUser(RequestCreateUser requestCreateUser, MultipartFile profileImage)
            throws BusinessException, IOException {

        isExistEmail(requestCreateUser.getEmail());
        User user = hasImage(profileImage, requestCreateUser.dtoToUserDomain(passwordEncoder));
        Member member = memberFactory.createUserOauthMember(user);
        addMemberUuid(user, member);
        return ResponseGetMember.of(member.getUser(), member.getId());
    }

    private void addMemberUuid(final User user, final Member member) {
        memberRepository.save(member);
        user.updateMemberUuid(member.getId());
        userRepository.save(user);
    }

    private void isExistEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new DuplicateEmailException(EMAIL_ALREADY_EXISTS, EMAIL_ALREADY_EXISTS.getMessage());
        });
    }

    private User hasImage(final MultipartFile multipartFile, final User user) throws IOException {
        if (multipartFile != null) {
            updateProfileImage(s3Uploader.upload(multipartFile, DIR_NAME), user);
        }
        return userRepository.save(user);
    }

    @Override
    public ResponseGetMember updateUser(UUID memberId, RequestUpdateUser requestUpdateUser) {
        Member findMember = getMemberByUuid(memberId);
        UserOauthMember userOauthMember = findMember.resolveMemberByLoginType();
        if (userOauthMember.isWeaver()) {
            return getResponseGetMemberWithUser(requestUpdateUser, findMember.getUser());
        }
        return getResponseGetMemberWithOauth(requestUpdateUser, findMember.getOauthMember());
    }

    private ResponseGetMember getResponseGetMemberWithOauth(final RequestUpdateUser requestUpdateUser,
                                                            final OauthUser oauthMember) {
        if (!requestUpdateUser.getType().equals(NICKNAME)) {
            throw new MismatchedInputException(MISMATCHED_INPUT_VALUE, MISMATCHED_INPUT_VALUE.getMessage());
        }
        oauthMember.updateNickname((String) requestUpdateUser.getValue());
        oauthMemberRepository.save(oauthMember);
        return ResponseGetMember.of(oauthMember, oauthMember.getMemberUuid());
    }

    private ResponseGetMember getResponseGetMemberWithUser(final RequestUpdateUser requestUpdateUser, final User user) {
        switch (requestUpdateUser.getType()) {
            case EMAIL -> user.updateEmail((String) requestUpdateUser.getValue());
            case NICKNAME -> user.updateNickname((String) requestUpdateUser.getValue());
            case PASSWORD -> updatePassword(requestUpdateUser.getValue(), user);
        }
        userRepository.save(user);
        return ResponseGetMember.of(user, user.getMemberUuid());
    }

    public ResponseSimpleURL updateProfile(final MultipartFile multipartFile, final UUID memberUUID) throws IOException {
        Member member = getMemberByUuid(memberUUID);
        UserOauthMember userOauthMember = member.resolveMemberByLoginType();
        String oldFileUrl = userOauthMember.getProfileImage().getPath().substring(1);
        updateProfileImage(s3Uploader.updateFile(multipartFile, oldFileUrl, DIR_NAME), userOauthMember);
        return new ResponseSimpleURL(userOauthMember.getProfileImage());
    }

    private Member getMemberByUuid(final UUID memberUUID) {
        return memberRepository.findById(memberUUID)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
    }

    private void updateProfileImage(final String s3Uploader, final UserOauthMember userOauthMember) throws IOException {
        URL updatedImageUrlObject = new URL(s3Uploader);
        userOauthMember.updateProfileImage(updatedImageUrlObject);
        memberStorageHandler.handle(userOauthMember);
    }

    private void updatePassword(final Object requestUpdateUser, final User user) {
        if (requestUpdateUser instanceof LinkedHashMap) {
            JSONObject jsonObject = new JSONObject((LinkedHashMap) requestUpdateUser);
            String currentPassword = (String) jsonObject.get(CURRENT_PASSWORD);
            String updatePassword = (String) jsonObject.get(UPDATE_PASSWORD);
            validateMatchedPassword(user, currentPassword);
            user.updatePassword(passwordEncoder.encode(updatePassword));
        }
    }

    private void validateMatchedPassword(final User user, final String currentPassword) {
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new MismatchedPassword(MISMATCHED_PASSWORD, MISMATCHED_PASSWORD.getMessage());
        }
    }

    @Override
    public void updateUser(final RequestUpdatePassword requestUpdatePassword) {
        UUID uuid = UUID.fromString(requestUpdatePassword.getUuid());
        Member member = getMemberByUuid(uuid);
        member.resolveMemberByLoginType().updatePassword(passwordEncoder.encode(requestUpdatePassword.getPassword()));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}
