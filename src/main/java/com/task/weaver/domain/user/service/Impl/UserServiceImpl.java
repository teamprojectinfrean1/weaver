package com.task.weaver.domain.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.*;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.ExistingEmailException;
import com.task.weaver.common.exception.user.UserNotFoundException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseUser getUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));
        return new ResponseUser(user);
    }

    @Override
    public ResponseUser getUser(String email) {
        // 예외처리
        User findUser = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));

        return new ResponseUser(findUser);
    }

    @Override
    public Boolean checkMail(String email) {
        if(userRepository.findByEmail(email).isPresent())
            return false;
        return true;
    }

    @Override
    public Boolean checkId(String id) {
        if(userRepository.findByUserId(id).isPresent())
            return false;
        return true;
    }

    @Override
    public List<ResponseGetUserList> getUsers(UUID projectId) throws BusinessException{
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(projectId))));

        List<User> users = userRepository.findUsersForProject(projectId)
                .orElseThrow(() -> new UserNotFoundException(new Throwable(String.valueOf(projectId))));

        List<ResponseGetUserList> responseGetUserLists = new ArrayList<>();

        for (User user : users)
            responseGetUserLists.add(new ResponseGetUserList(user));

        return responseGetUserLists;
    }

    @Override
    public List<ResponseUser> getUsers(Project project) {
        return null;
    }

    @Override
    public List<ResponseUser> getUsers(User user) {
        return null;
    }

    @Override
    public List<ResponseUser> getUsers(Story story) {
        return null;
    }

    @Override
    public ResponseUser addUser(RequestCreateUser requestCreateUser) throws BusinessException {

        isExistEmail(requestCreateUser.getEmail());

        User user = User.builder()
                .id(requestCreateUser.getId())
                .nickname(requestCreateUser.getNickname())
                .email(requestCreateUser.getEmail())
                .password(passwordEncoder.encode(requestCreateUser.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        log.info("user uuid : " + savedUser.getUserId());
        return new ResponseUser(savedUser);
    }

    private void isExistEmail(String email) {
        // log.info("service - join - isExistEmail - ing"+ userRepository.findByEmail(email).isPresent());
        userRepository.findByEmail(email).ifPresent(user -> {
            log.debug("userId : {}, 아이디 중복으로 회원가입 실패", email);
            throw new RuntimeException("아이디 중복");
        });
    }

    @Override
    public ResponseUser updateUser(UUID userId, RequestUpdateUser requestUpdateUser) {
        User findUser = userRepository.findById(userId).get();
        findUser.updateUser(requestUpdateUser);

        return new ResponseUser(findUser);
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
