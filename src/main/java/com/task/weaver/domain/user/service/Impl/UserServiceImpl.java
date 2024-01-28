package com.task.weaver.domain.user.service.Impl;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Override
    public Optional<User> getUser(Long user_id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getUser(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getUsers(Long project_id) {
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getUsers(Project project) {
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getUsers(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> getUsers(Story story) {
        return Optional.empty();
    }

    @Override
    public Optional<User> addUser(User user) {
        return Optional.empty();
    }

    @Override
    public Optional<List<User>> addUsers() {
        return Optional.empty();
    }

    @Override
    public Optional<User> updateUser(Long user_id) {
        return Optional.empty();
    }

    @Override
    public void deleteUser(Long user_id) {

    }

    @Override
    public void deleteUser(User user) {

    }
}
