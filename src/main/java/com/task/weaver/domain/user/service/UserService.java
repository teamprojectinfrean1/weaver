package com.task.weaver.domain.user.service;

import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.user.entity.User;
import com.task.weaver.domain.user.dto.response.EmailVerificationResult;
import java.util.List;
import java.util.UUID;

import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    ResponseGetUser getUser(UUID userId);
    ResponseGetUser getUser(String email);

    ResponseGetUser getUserFromToken(HttpServletRequest request);

    ResponsePageResult<ResponseGetUserList, User> getUsers(RequestGetUserPage requestGetUserPage);
    List<ResponseGetUser> getUsersForTest();
    ResponsePageResult<ResponseGetUserList, User> getUsersForSearch(String nickname);
    List<ResponseGetUser> getUsers(Project project); //프로젝트에 연괸된 사람들
    List<ResponseGetUser> getUsers(User user);  //본인과 연결된 다른 사람들
    //Optional<List<User>> getUsers(Task task); //task와 연결된 사람들

    ResponseGetUser addUser(RequestCreateUser requestCreateUser);

    ResponseGetUser updateUser(UUID userId, RequestUpdateUser requestUpdateUser);
    void deleteUser(UUID userId);
    void deleteUser(User user);
}
