package com.task.weaver.domain.user.service;

import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.user.dto.response.*;
import com.task.weaver.domain.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface UserService {

    ResponseUuid getUuid(String email, Boolean checked);

    ResponseGetUser getUser(UUID userId);

    ResponseGetUser getUserByMail(String email);

    ResponseGetUser getUserById(String id);

    ResponseUserIdNickname getUser(String email, Boolean checked);

    ResponseGetUserForFront getUserFromToken(HttpServletRequest request);

    ResponseUserMypage getUserInfo(String userId);

    ResponsePageResult<ResponseGetUserList, User> getUsers(RequestGetUserPage requestGetUserPage);
    List<ResponseGetUser> getUsersForTest();
    ResponsePageResult<ResponseGetUserList, User> getUsersForSearch(String nickname);
    List<ResponseGetUser> getUsers(Project project); //프로젝트에 연괸된 사람들
    List<ResponseGetUser> getUsers(User user);  //본인과 연결된 다른 사람들
    //Optional<List<User>> getUsers(Task task); //task와 연결된 사람들

    ResponseGetUser addUser(RequestCreateUser requestCreateUser, MultipartFile multipartFile) throws IOException;

    ResponseGetUser updateUser(UUID userId, RequestUpdateUser requestUpdateUser)
            throws IOException, ParseException;
    void updateUser(RequestUpdatePassword requestUpdateUser);
    void deleteUser(UUID userId);
    void deleteUser(User user);
}
