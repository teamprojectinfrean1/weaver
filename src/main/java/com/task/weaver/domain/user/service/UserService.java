package com.task.weaver.domain.user.service;

import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.story.entity.Story;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.dto.response.ResponseUser;
import com.task.weaver.domain.user.entity.User;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    ResponseUser getUser(UUID userId);
    ResponseUser getUser(String email);

    ResponseUser getUserFromToken(HttpServletRequest request);

    ResponsePageResult<ResponseGetUserList, User> getUsers(RequestGetUserPage requestGetUserPage);
    ResponsePageResult<ResponseGetUserList, User> getUsersForSearch(String nickname);
    List<ResponseUser> getUsers(Project project); //프로젝트에 연괸된 사람들
    List<ResponseUser> getUsers(User user);  //본인과 연결된 다른 사람들
    //Optional<List<User>> getUsers(Task task); //task와 연결된 사람들
    List<ResponseUser> getUsers(Story story); //story에 연괸된 사람들

    ResponseUser addUser(RequestCreateUser requestCreateUser);

    ResponseUser updateUser(UUID userId, RequestUpdateUser requestUpdateUser);
    void deleteUser(UUID userId);
    void deleteUser(User user);

}
