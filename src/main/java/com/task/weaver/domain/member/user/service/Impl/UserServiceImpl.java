package com.task.weaver.domain.member.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.MISMATCHED_PASSWORD;
import static com.task.weaver.common.exception.ErrorCode.NO_MATCHED_VERIFICATION_CODE;
import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;
import static com.task.weaver.domain.useroauthmember.dto.response.ResponseUserOauth.*;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.MismatchedPassword;
import com.task.weaver.common.exception.user.UnableSendMailException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.common.jwt.provider.JwtTokenProvider;
import com.task.weaver.common.redis.service.RedisService;
import com.task.weaver.common.s3.S3Uploader;
import com.task.weaver.domain.member.LoginType;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.member.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.member.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.member.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.member.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.member.user.dto.response.ResponseUserMypage;
import com.task.weaver.domain.member.user.dto.response.ResponseUuid;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.member.user.repository.UserRepository;
import com.task.weaver.domain.member.user.service.UserService;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.useroauthmember.entity.UserOauthMember;
import com.task.weaver.domain.useroauthmember.factory.UserOauthMemberFactory;
import com.task.weaver.domain.useroauthmember.repository.UserOauthMemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Uploader s3Uploader;
    private final RedisService redisService;
    private final UserOauthMemberFactory userOauthMemberFactory;
    private final UserOauthMemberRepository userOauthMemberRepository;

    @Override
    public ResponseUuid getUuid(final String email, final Boolean checked) {
        if (!checked) {
            throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, ": Redis to SMTP DATA");
        }

        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

        return ResponseUuid.builder()
                .uuid(findUser.getUserId())
                .build();
    }

    @Override
    public ResponseGetUser getUser(UUID memberId) {
        UserOauthMember member = userOauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new UsernameNotFoundException(USER_NOT_FOUND.getMessage()));
        return new ResponseGetUser(member.getUser(), member.getId());
    }

    @Override
    public ResponseUserIdNickname getUser(String email, Boolean checked) {
        if (!checked)
            throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, ": Redis to SMTP DATA");

        User findUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

        return ResponseUserIdNickname.builder()
                .id(findUser.getId())
                .nickname(findUser.getNickname())
                .build();
    }

    @Override
    public ResponseGetUserForFront getUserFromToken(HttpServletRequest request) {
        String userId = jwtTokenProvider.getMemberIdByAssessToken(request);
        log.info("user id : " + userId);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(userId))));
        return new ResponseGetUserForFront(user);
    }

    @Override
    public ResponseUserMypage getUserInfo(final String userId) {
        return userRepository.findUserInfoByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_EMAIL_NOT_FOUND, "해당 UUID로 사용자를 찾을 수 없습니다."));
    }

    @Override
    public ResponsePageResult<ResponseGetUserList, User> getUsers(RequestGetUserPage requestGetUserPage) throws BusinessException{
        UUID projectId = requestGetUserPage.getProjectId();

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

        Pageable pageable = requestGetUserPage.getPageable(Sort.by("userId").descending());

        Page<User> users = userRepository.findUsersForProject(projectId, requestGetUserPage.getNickname(), pageable);

        Function<User, ResponseGetUserList> fn = (ResponseGetUserList::new);

        return new ResponsePageResult<>(users, fn);
    }

    @Override
    public AllMember getUsersForTest() {
        List<UserOauthMember> userOauthMembers = userOauthMemberRepository.findAll();
        List<User> users = userOauthMembers
                .stream()
                .map(UserOauthMember::getUser).toList();
        List<OauthMember> members = userOauthMembers
                .stream()
                .map(UserOauthMember::getOauthMember).toList();
        return AllMember.create(users, members);
    }

    @Override
    public ResponsePageResult<ResponseGetUserList, User> getUsersForSearch(String nickname) {
        return null;
    }

    @Override
    public List<ResponseGetUser> getUsers(Project project) {
        return null;
    }

    @Override
    public List<ResponseGetUser> getUsers(User user) {
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseGetUser addUser(RequestCreateUser requestCreateUser, MultipartFile profileImage) throws BusinessException, IOException {

        isExistEmail(requestCreateUser.getEmail());

        User user = User.builder()
                .id(requestCreateUser.getId())
                .nickname(requestCreateUser.getNickname())
                .email(requestCreateUser.getEmail())
                .password(passwordEncoder.encode(requestCreateUser.getPassword()))
                .build();
        hasGetImage(profileImage, user);
        User savedUser = userRepository.save(user);
        UserOauthMember userOauthMember = userOauthMemberFactory.createUserOauthMember(user);
        log.info("user uuid : " + savedUser.getUserId());
        return new ResponseGetUser(userOauthMember.getUser(), userOauthMember.getId());
    }

    private void hasGetImage(final MultipartFile profileImage, final User user) throws IOException {
        if (profileImage != null) {
            updateProfileImage(s3Uploader.upload(profileImage, "images"), user);
        }
    }

    private void updateProfileImage(final String s3Uploader, final User user) throws IOException {
        URL updatedImageUrlObject = new URL(s3Uploader);
        user.updateProfileImage(updatedImageUrlObject);
    }

    private void isExistEmail(String email) {
        // log.info("service - join - isExistEmail - ing"+ userRepository.findByEmail(email).isPresent());
        userRepository.findByEmail(email).ifPresent(user -> {
            log.debug("userId : {}, 아이디 중복으로 회원가입 실패", email);
            throw new RuntimeException("아이디 중복");
        });
    }

    @Override
    public ResponseGetUser updateUser(UUID memberId, RequestUpdateUser requestUpdateUser) throws IOException {
        UserOauthMember findMember = userOauthMemberRepository.findById(memberId)
                .orElseThrow(() -> new UserNotFoundException(
                        USER_NOT_FOUND, "해당 유저를 찾을 수 없습니다."));

        if (findMember.getLoginType().equals(LoginType.WEAVER)) {
            User user = findMember.getUser();
            switch (requestUpdateUser.getType()) {
                case "email" -> user.updateEmail((String) requestUpdateUser.getValue());
                case "nickname" -> user.updateNickname((String) requestUpdateUser.getValue());
                case "password" -> updatePassword(requestUpdateUser.getValue(), user);
                case "profileImage" -> updateProfile(requestUpdateUser.getValue(), user);
            }
            userRepository.save(user);
            return new ResponseGetUser(user, findMember.getId());
        }
        throw new UserNotFoundException(USER_NOT_FOUND, "해당 유저가 존재하지않습니다.");
    }

    private void updateProfile(final Object value, final User user) throws IOException {
        String oldFileUrl = user.getProfileImage().getPath().substring(1);
        updateProfileImage(s3Uploader.updateFile((MultipartFile) value, oldFileUrl, "images"), user);
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

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
