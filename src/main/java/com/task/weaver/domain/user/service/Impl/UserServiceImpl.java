package com.task.weaver.domain.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.*;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.project.ProjectNotFoundException;
import com.task.weaver.common.exception.user.ExistingEmailException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.project.repository.ProjectRepository;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ResponseUser getUser(Long user_id) {
        User user = userRepository.findById(user_id)
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
    public List<ResponseUser> getUsers(Long project_id) throws BusinessException{
        Project project = projectRepository.findById(project_id)
                .orElseThrow(() -> new ProjectNotFoundException(new Throwable(String.valueOf(project_id))));


        return null;
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
                .nickname(requestCreateUser.getNickname())
                .email(requestCreateUser.getEmail())
                .password(passwordEncoder.encode(requestCreateUser.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

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
    public ResponseUser updateUser(Long user_id, RequestUpdateUser requestUpdateUser) {
        User findUser = userRepository.findById(user_id).get();
        findUser.updateUser(requestUpdateUser);

        return new ResponseUser(findUser);
    }

    @Override
    public void deleteUser(Long user_id) {
        userRepository.deleteById(user_id);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
