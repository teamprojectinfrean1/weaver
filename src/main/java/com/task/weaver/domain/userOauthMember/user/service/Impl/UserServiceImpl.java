package com.task.weaver.domain.userOauthMember.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.*;
import static com.task.weaver.common.exception.ErrorCode.MISMATCHED_PASSWORD;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.authorization.InvalidPasswordException;
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

        User user = userRepository.findByUserId(requestSignIn.id())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, " 해당 ID가 존재하지 않습니다."));
        hasMatched(requestSignIn);
        Member byUser = memberRepository.findByUser(user)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, " 해당 유저가 존재하지 않습니다."));
        return memberService.getAuthentication(byUser);
    }

    private void hasMatched(final RequestSignIn requestSignIn) {
        if (!checkPassword(requestSignIn)) {
            throw new InvalidPasswordException(new Throwable(requestSignIn.password()));
        }
    }

    private boolean checkPassword(RequestSignIn requestSignIn) {
        User user = userRepository.findByUserId(requestSignIn.id())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, " 해당 id가 존재하지 않습니다."));
        log.info(user.getPassword());
        log.info(requestSignIn.password());
        log.info(passwordEncoder.matches(requestSignIn.password(), user.getPassword()) ? "true" : "false");
        return passwordEncoder.matches(requestSignIn.password(), user.getPassword()); // 암호화된 비밀번호가 뒤로 와야 한다 순서
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseGetMember addUser(RequestCreateUser requestCreateUser, MultipartFile profileImage)
            throws BusinessException, IOException {

        isExistEmail(requestCreateUser.getEmail());
        User user = hasImage(profileImage, requestCreateUser.dtoToUserDomain(passwordEncoder));
        Member member = memberFactory.createUserOauthMember(user);
        addMemberUuid(user, member);
        log.info("user uuid : " + user.getUserId());
        return ResponseGetMember.of(member.getUser());
    }

    private void addMemberUuid(final User user, final Member member) {
        user.updateMemberUuid(member.getId());
        userRepository.save(user);
    }

    private void isExistEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            log.debug("userId : {}, 이메일 중복으로 회원가입 실패", email);
            throw new RuntimeException("이메일 중복");
        });
    }

    private User hasImage(final MultipartFile multipartFile, final User user) throws IOException {
        if (multipartFile != null) {
            updateProfileImage(s3Uploader.upload(multipartFile, "images"), user);
        }
        return userRepository.save(user);
    }

    @Override
    public ResponseGetMember updateUser(UUID memberId, RequestUpdateUser requestUpdateUser) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        UserOauthMember userOauthMember = findMember.resolveMemberByLoginType();
        if (userOauthMember.isWeaver()) {
            return getResponseGetMemberWithUser(requestUpdateUser, findMember.getUser());
        }
        return getResponseGetMemberWithOauth(requestUpdateUser, findMember.getOauthMember());
    }

    private ResponseGetMember getResponseGetMemberWithOauth(final RequestUpdateUser requestUpdateUser,
                                                            final OauthUser oauthMember) {
        if (!requestUpdateUser.getType().equals("nickname")) {
            throw new MismatchedInputException(
                    MISMATCHED_INPUT_VALUE, MISMATCHED_INPUT_VALUE.getMessage());
        }
        oauthMember.updateNickname((String) requestUpdateUser.getValue());
        log.info("update nickname = {}", requestUpdateUser.getValue());
        oauthMemberRepository.save(oauthMember);
        return ResponseGetMember.of(oauthMember);
    }

    private ResponseGetMember getResponseGetMemberWithUser(final RequestUpdateUser requestUpdateUser, final User user) {
        switch (requestUpdateUser.getType()) {
            case "email" -> user.updateEmail((String) requestUpdateUser.getValue());
            case "nickname" -> user.updateNickname((String) requestUpdateUser.getValue());
            case "password" -> updatePassword(requestUpdateUser.getValue(), user);
        }
        userRepository.save(user);
        return ResponseGetMember.of(user);
    }

    public ResponseSimpleURL updateProfile(final MultipartFile multipartFile, final UUID memberUUID) throws IOException {
        Member member = memberRepository.findById(memberUUID)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        UserOauthMember userOauthMember = member.resolveMemberByLoginType();
        String oldFileUrl = userOauthMember.getProfileImage().getPath().substring(1);
        log.info("user profile origin = {}", userOauthMember.getProfileImage().getPath());
        log.info("oldFileUrl = {}", oldFileUrl);
        updateProfileImage(s3Uploader.updateFile(multipartFile, oldFileUrl, "images"), userOauthMember);
        return new ResponseSimpleURL(userOauthMember.getProfileImage());
    }

    private void updateProfileImage(final String s3Uploader, final UserOauthMember userOauthMember) throws IOException {
        URL updatedImageUrlObject = new URL(s3Uploader);
        userOauthMember.updateProfileImage(updatedImageUrlObject);
        memberStorageHandler.handle(userOauthMember);
    }

    private void updatePassword(final Object requestUpdateUser, final User user) {
        if (requestUpdateUser instanceof LinkedHashMap) {
            JSONObject jsonObject = new JSONObject((LinkedHashMap) requestUpdateUser);
            String currentPassword = (String) jsonObject.get("currentPassword");
            String updatePassword = (String) jsonObject.get("updatePassword");
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
        Member member = memberRepository.findById(uuid).orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, USER_NOT_FOUND.getMessage()));
        member.resolveMemberByLoginType().updatePassword(passwordEncoder.encode(requestUpdatePassword.getPassword()));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}
