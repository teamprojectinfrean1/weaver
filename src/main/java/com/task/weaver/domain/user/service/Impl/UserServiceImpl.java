package com.task.weaver.domain.user.service.Impl;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public ResponseUser getUser(Long user_id) {
        User user = userRepository.findById(user_id).get();  //예외추가 필요.
        return new ResponseUser(user);
    }

    @Override
    public ResponseUser getUser(String nickname) {
        User findUser = userRepository.findByName(nickname).get();

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
    public ResponseUser addUser(RequestCreateUser requestCreateUser) {
        User user = requestCreateUser.toEntity();
        User savedUser = userRepository.save(user);

        return new ResponseUser(savedUser);
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
