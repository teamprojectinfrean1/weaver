package com.task.weaver.domain.user.service.Impl;

import static com.task.weaver.common.exception.ErrorCode.*;

import com.task.weaver.common.exception.BusinessException;
import com.task.weaver.common.exception.ErrorCode;
import com.task.weaver.common.exception.user.ExistingEmailException;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
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
    public List<ResponseUser> getUsers(Long project_id) {
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

        if(!isExistEmail(requestCreateUser.getEmail())) {
            throw new ExistingEmailException(new Throwable(requestCreateUser.getEmail()));
        }

        User user = User.builder()
            .name(requestCreateUser.getName())
            .email(requestCreateUser.getEmail())
            .password(passwordEncoder.encode(requestCreateUser.getPassword()))
            .build();


        User savedUser = userRepository.save(user);

        return new ResponseUser(savedUser);
    }

    private boolean isExistEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
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

    /**
     * Spring Security 유저 인증 처리 과정 중 유저 객체 만드는 과정
     * ❕ 보통 UserDetails를 따로 만들어서 사용하지만 UserDetails 인터페이스를 구현한 User라는 클래스를 시큐리티가 제공해주긴함
     * 그럴려면 Entity를 Member라고 바꿔야함 (User 겹쳐서 사용 못함)
     * @param email the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException(USER_EMAIL_NOT_FOUND.getMessage()));
    }

/**
 * Security 권한 관리
 * 현재 서비스는 프로젝트마다 권한 관리를 해줘야하기 때문에 패스
 */
    // private UserDetails createUserDetails(User user) {
    //     List<SimpleGrantedAuthority> grantedAuthorities = user.getRoleList()
    //         .stream()
    //         .map(authority -> new SimpleGrantedAuthority(authority))
    //         .collect(Collectors.toList());
    //
    //     return new User(user.getUserId(),
    //         user.getPassword(),
    //         grantedAuthorities);
    // }
}
