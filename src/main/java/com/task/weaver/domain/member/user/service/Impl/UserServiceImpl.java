package com.task.weaver.domain.member.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.MISMATCHED_PASSWORD;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.authorization.InvalidPasswordException;
import com.task.weaver.common.exception.member.MismatchedPassword;
import com.task.weaver.common.exception.member.UserNotFoundException;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.entity.Member;
import com.task.weaver.domain.authorization.factory.MemberFactory;
import com.task.weaver.domain.authorization.repository.MemberRepository;
import com.task.weaver.domain.authorization.service.MemberService;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.oauth.entity.OauthUser;
import com.task.weaver.domain.member.oauth.repository.OauthMemberRepository;
import com.task.weaver.domain.member.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.member.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.member.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import com.task.weaver.domain.member.user.service.UserService;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Objects;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;
    private final MemberFactory memberFactory;
    private final MemberRepository userOauthMemberRepository;
    private final OauthMemberRepository oauthMemberRepository;
    private final MemberService memberService;


    @Override
    @Transactional(readOnly = true)
    public ResponseToken weaverLogin(RequestSignIn requestSignIn) {

        User user = userRepository.findByUserId(requestSignIn.id())
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, " 해당 ID가 존재하지 않습니다."));
        hasMatched(requestSignIn);
        Member byUser = userOauthMemberRepository.findByUser(user)
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
        User user = hasImage(profileImage, requestCreateUser.toDomain(passwordEncoder));
        Member member = memberFactory.createUserOauthMember(user);
        log.info("user uuid : " + user.getUserId());
        return ResponseGetMember.of(member.getUser());
    }

    private void isExistEmail(String email) {
        userRepository.findByEmail(email).ifPresent(user -> {
            log.debug("userId : {}, 이메일 중복으로 회원가입 실패", email);
            throw new RuntimeException("이메일 중복");
        });
    }

    private User hasImage(final MultipartFile profileImage, final User user) throws IOException {
        if (profileImage != null) {
            updateProfileImage(s3Uploader.upload(profileImage, "images"), user);
        }
        return userRepository.save(user);
    }

    @Override
    public ResponseGetMember updateUser(UUID memberId, RequestUpdateUser requestUpdateUser) throws IOException {
        Member findMember = userOauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));
        if (findMember.getLoginType().equals(LoginType.WEAVER)) {
            return getResponseGetMemberWithUser(requestUpdateUser, findMember.getUser());
        }
        return getResponseGetMemberWithOauth(requestUpdateUser, findMember.getOauthMember());
    }

    private ResponseGetMember getResponseGetMemberWithOauth(final RequestUpdateUser requestUpdateUser,
                                                            final OauthUser oauthMember) throws IOException {
        switch (requestUpdateUser.getType()) {
            case "nickname" -> oauthMember.updateNickname((String) requestUpdateUser.getValue());
            case "profileImage" -> updateProfile(requestUpdateUser.getValue(), oauthMember);
        }
        oauthMemberRepository.save(oauthMember);
        return ResponseGetMember.of(oauthMember);
    }

    private ResponseGetMember getResponseGetMemberWithUser(final RequestUpdateUser requestUpdateUser, final User user)
            throws IOException {
        switch (requestUpdateUser.getType()) {
            case "email" -> user.updateEmail((String) requestUpdateUser.getValue());
            case "nickname" -> user.updateNickname((String) requestUpdateUser.getValue());
            case "password" -> updatePassword(requestUpdateUser.getValue(), user);
            case "profileImage" -> updateProfile(requestUpdateUser.getValue(), user);
        }
        userRepository.save(user);
        return ResponseGetMember.of(user);
    }

    private void updateProfile(final Object value, final UserOauthMember userOauthMember) throws IOException {
        String oldFileUrl = userOauthMember.getProfileImage().getPath().substring(1);
        updateProfileImage(s3Uploader.updateFile((MultipartFile) value, oldFileUrl, "images"), userOauthMember);
    }


    private void updateProfileImage(final String s3Uploader, final UserOauthMember userOauthMember) throws IOException {
        URL updatedImageUrlObject = new URL(s3Uploader);
        userOauthMember.updateProfileImage(updatedImageUrlObject);
    }

    private void updatePassword(final Object requestUpdateUser, final User user) {

        if (requestUpdateUser instanceof LinkedHashMap) {
            JSONObject jsonObject = new JSONObject((LinkedHashMap) requestUpdateUser);
            String currentPassword = (String) jsonObject.get("currentPassword");
            String updatePassword = (String) jsonObject.get("updatePassword");

            if (!Objects.equals(user.getPassword(), currentPassword)) {
                throw new MismatchedPassword(MISMATCHED_PASSWORD, "입력 값 확인이 필요합니다.");
            }
            user.updatePassword(passwordEncoder.encode(updatePassword));
        }
    }

    @Transactional
    @Override
    public void updateUser(final RequestUpdatePassword requestUpdatePassword) {
        UUID uuid = UUID.fromString(requestUpdatePassword.getUuid());
        Optional<User> byUserId = userRepository.findById(uuid);
        User user = byUserId.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, ": 해당 유저를 찾을 수 없습니다."));
        user.updatePassword(passwordEncoder.encode(requestUpdatePassword.getPassword()));
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}
