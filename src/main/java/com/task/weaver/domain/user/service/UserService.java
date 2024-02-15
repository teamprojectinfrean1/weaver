package com.task.weaver.domain.user.service;

import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.entity.User;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    ResponseUser getUser(Long user_id);
    ResponseUser getUser(String email);

    List<ResponseUser> getUsers(Long project_id);
    List<ResponseUser> getUsers(Project project); //프로젝트에 연괸된 사람들
    List<ResponseUser> getUsers(User user);  //본인과 연결된 다른 사람들
    //Optional<List<User>> getUsers(Task task); //task와 연결된 사람들
    List<ResponseUser> getUsers(Story story); //story에 연괸된 사람들

    ResponseUser addUser(RequestCreateUser requestCreateUser);

    ResponseUser updateUser(Long user_id, RequestUpdateUser requestUpdateUser);
    void deleteUser(Long user_id);
    void deleteUser(User user);
}
