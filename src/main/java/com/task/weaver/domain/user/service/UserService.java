package com.task.weaver.domain.user.service;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUser(Long user_id);
    Optional<User> getUser(User user);

    Optional<List<User>> getUsers(Long project_id);
    Optional<List<User>> getUsers(Project project); //프로젝트에 연괸된 사람들
    Optional<List<User>> getUsers(User user);  //본인과 연결된 다른 사람들
    //Optional<List<User>> getUsers(Task task); //task와 연결된 사람들
    Optional<List<User>> getUsers(Story story); //story에 연괸된 사람들


    Optional<User> addUser(User user);
    Optional<List<User>> addUsers();

    Optional<User> updateUser(Long user_id);
    void deleteUser(Long user_id);
    void deleteUser(User user);
}
