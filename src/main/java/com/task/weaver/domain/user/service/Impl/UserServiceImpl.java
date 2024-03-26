package com.task.weaver.domain.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.*;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.user.UnableSendMailException;
import com.task.weaver.domain.authorization.service.impl.RedisService;
import com.task.weaver.domain.mail.service.impl.MailServiceImpl;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.authorization.util.JwtTokenProvider;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.EmailVerificationResult;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String AUTH_CODE_PREFIX = "AuthCode ";
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailServiceImpl mailService;
    private final RedisService redisService;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    @Override
    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "Weaver-TaskGram 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendMail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 Redis에 저장 ( key = "AuthCode " + Email / value = AuthCode )
        redisService.setValues(AUTH_CODE_PREFIX + toEmail,
                authCode, Duration.ofMillis(this.authCodeExpirationMillis));
    }

    @Override
    public void checkDuplicatedEmail(String email) {
        Optional<User> member = userRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new UsernameNotFoundException(USER_NOT_FOUND.getMessage());
        }
    }

    @Override
    public String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new UnableSendMailException(new Throwable(String.valueOf(ErrorCode.NO_SEARCH_ALGORITHM)));
        }
    }

    @Override
    public EmailVerificationResult verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = redisService.getValues(AUTH_CODE_PREFIX + email);
        boolean authResult = redisService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
        return EmailVerificationResult.of(authResult);
    }

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public ResponseGetUser getUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));
        return new ResponseGetUser(user);
    }

    @Override
    public ResponseGetUser getUser(String email) {
        // 예외처리
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

        return new ResponseGetUser(findUser);
    }

    @Override
    public ResponseGetUser getUserFromToken(HttpServletRequest request) {
        String userId = jwtTokenProvider.getUsername(request);
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(userId))));
        ResponseGetUser responseGetUser = new ResponseGetUser(user);
        return responseGetUser;
    }

//    @Override
//    public Boolean checkMail(String email) {
//        if(userRepository.findByEmail(email).isPresent())
//            return false;
//        return true;
//    }
//
//    @Override
//    public Boolean checkId(String id) {
//        if(userRepository.findByUserId(id).isPresent())
//            return false;
//        return true;
//    }

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

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
