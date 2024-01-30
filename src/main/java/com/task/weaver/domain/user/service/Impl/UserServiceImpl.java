package com.task.weaver.domain.user.service.Impl;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.dto.reesponse.ResponseUser;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.repository.UserRepository;
import com.task.weaver.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public ResponseUser getUser(Long user_id) {
        User user = userRepository.findById(user_id).get();  //예외추가 필요.

        ResponseUser responseUser = new ResponseUser(user);

        return responseUser;
    }

    @Override
    public ResponseUser getUser(String nickname) {
        User findUser = userRepository.findByNickname(nickname).get();
        ResponseUser responseUser = new ResponseUser(findUser);

        return responseUser;
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

        ResponseUser responseUser = new ResponseUser(savedUser);
        return responseUser;
    }

    @Override
    public ResponseUser updateUser(Long user_id, RequestUpdateUser requestUpdateUser) {
        User findUser = userRepository.findById(user_id).get();
        findUser.updateUser(requestUpdateUser);

        ResponseUser responseUser = new ResponseUser(findUser);
        return responseUser;
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
