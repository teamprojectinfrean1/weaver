package com.task.weaver.domain.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.NO_MATCHED_VERIFICATION_CODE;
import static com.task.weaver.common.exception.ErrorCode.NO_SEARCH_EMAIL;
import static com.task.weaver.common.exception.ErrorCode.USER_EMAIL_NOT_FOUND;
import static com.task.weaver.common.exception.ErrorCode.USER_NOT_FOUND;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
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
import com.task.weaver.domain.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public ResponseGetUser getUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));
        return new ResponseGetUser(user);
    }

    @Override
    public ResponseGetUser getUser(final String email) {
        User findUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(USER_EMAIL_NOT_FOUND, ": 해당 이메일이 존재하지않습니다."));
        return new ResponseGetUser(findUser);
    }

    @Override
    public ResponseUserIdNickname getUser(String email, Boolean checked) {
        if (!checked) {
            throw new UnableSendMailException(NO_MATCHED_VERIFICATION_CODE, ": Redis to SMTP DATA", checked);
        }

        User findUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

        return ResponseUserIdNickname.builder()
                .uuid(findUser.getUserId())
                .id(findUser.getId())
                .userNickname(findUser.getNickname())
                .data(checked)
                .build();
    }

    @Override
    public ResponseGetUser getUserFromToken(HttpServletRequest request) {
        String userId = jwtTokenProvider.getUsername(request);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(userId))));
        ResponseGetUser responseGetUser = new ResponseGetUser(user);
        return responseGetUser;
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
        User findUser = userRepository.findById(userId).get();
        findUser.updateUser(requestUpdateUser);

        return new ResponseGetUser(findUser);
    }

    @Transactional
    @Override
    public void updateUser(final RequestUpdatePassword requestUpdatePassword) {
        Optional<User> byUserId = userRepository.findByEmail(requestUpdatePassword.getEmail());
        User user = byUserId.orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND, ": 해당 유저를 찾을 수 없습니다."));
        user.updatePassword(requestUpdatePassword);
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
