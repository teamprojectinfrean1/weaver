package com.task.weaver.domain.user.service.Impl;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.MismatchedPassword;
import com.task.weaver.common.exception.user.UnableSendMailException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.authorization.util.JwtTokenProvider;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.*;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

import java.util.*;
import java.util.function.Function;

import static com.task.weaver.common.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseUuid getUuid(final String email, final Boolean checked) {
        if (!checked)
            throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, ": Redis to SMTP DATA");

        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

        return ResponseUuid.builder()
                .uuid(findUser.getUserId())
                .isSuccess(checked)
                .build();
    }

    @Override
    public ResponseGetUser getUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));
        return new ResponseGetUser(user);
    }
    @Override
    public ResponseGetUser getUserByMail(final String email) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_EMAIL_NOT_FOUND, ": 해당 이메일이 존재하지않습니다."));
        return new ResponseGetUser(findUser);
    }

    @Override
    public ResponseGetUser getUserById(String id) {
        User findUser = userRepository.findByUserId(id)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, ": 해당 ID가 존재하지 않습니다."));

        return new ResponseGetUser(findUser);
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
                .isSuccess(checked)
                .build();
    }

    @Override
    public ResponseGetUserForFront getUserFromToken(HttpServletRequest request) {
        String userId = jwtTokenProvider.getUsername(request);
        log.info("user id : " + userId);
        User user = userRepository.findByEmail(userId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(userId))));
        ResponseGetUserForFront responseGetUser = new ResponseGetUserForFront(user);
        return responseGetUser;
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

        Function<User, ResponseGetUserList> fn = ((User) -> new ResponseGetUserList(User));

        return new ResponsePageResult<>(users, fn);
    }

    @Override
    public List<ResponseGetUser> getUsersForTest() {
        List<User> users = userRepository.findAll();
        List<ResponseGetUser> responseGetUsers = new ArrayList<>();

        for (User user : users) {
            ResponseGetUser responseGetUser = new ResponseGetUser(user);
            responseGetUsers.add(responseGetUser);
        }
        return responseGetUsers;
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
    public ResponseGetUser addUser(RequestCreateUser requestCreateUser) throws BusinessException {

        isExistEmail(requestCreateUser.getEmail());

        User user = User.builder()
                .id(requestCreateUser.getId())
                .nickname(requestCreateUser.getNickname())
                .email(requestCreateUser.getEmail())
                .password(passwordEncoder.encode(requestCreateUser.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        log.info("user uuid : " + savedUser.getUserId());
        return new ResponseGetUser(savedUser);
    }

    private void isExistEmail(String email) {
        // log.info("service - join - isExistEmail - ing"+ userRepository.findByEmail(email).isPresent());
        userRepository.findByEmail(email).ifPresent(user -> {
            log.debug("userId : {}, 아이디 중복으로 회원가입 실패", email);
            throw new RuntimeException("아이디 중복");
        });
    }

    @Override
    public ResponseGetUser updateUser(UUID userId, RequestUpdateUser requestUpdateUser) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isPresent()) {
            User user = findUser.get();
            switch (requestUpdateUser.getType()) {
                case "email" -> user.updateEmail((String) requestUpdateUser.getValue());
                case "nickname" -> user.updateNickname((String) requestUpdateUser.getValue());
                case "password" -> updatePassword(requestUpdateUser.getValue(), user);
            }
            userRepository.save(user);
            return new ResponseGetUser(user);
        }
        throw new UserNotFoundException(USER_NOT_FOUND, "해당 유저가 존재하지않습니다.");
    }

    private void updatePassword(final Object requestUpdateUser, final User user) {

        if (requestUpdateUser instanceof LinkedHashMap) {
            JSONObject jsonObject = new JSONObject((LinkedHashMap) requestUpdateUser);
            String currentPassword = (String) jsonObject.get("currentPassword");
            String updatePassword = (String) jsonObject.get("updatePassword");

            if (!Objects.equals(user.getPassword(), currentPassword)) {
                throw new MismatchedPassword(MISMATCHED_PASSWORD, "입력 값 확인이 필요합니다.");
            }
            user.updatePassword(updatePassword);
        }
    }

    @Transactional
    @Override
    public void updateUser(final RequestUpdatePassword requestUpdatePassword) {
        UUID uuid = UUID.fromString(requestUpdatePassword.getUuid());
        Optional<User> byUserId = userRepository.findById(uuid);
        User user = byUserId.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, ": 해당 유저를 찾을 수 없습니다."));
        user.updatePassword(requestUpdatePassword.getPassword());
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
