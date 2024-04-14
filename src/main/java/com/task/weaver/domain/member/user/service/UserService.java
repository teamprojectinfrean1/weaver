package com.task.weaver.domain.member.user.service;

import com.task.weaver.domain.member.user.dto.response.ResponseGetUser;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserForFront;
import com.task.weaver.domain.member.user.dto.response.ResponseGetUserList;
import com.task.weaver.domain.member.user.dto.response.ResponseUserIdNickname;
import com.task.weaver.domain.member.user.dto.response.ResponseUserMypage;
import com.task.weaver.domain.member.user.dto.response.ResponseUuid;
import com.task.weaver.domain.project.dto.response.ResponsePageResult;
import com.task.weaver.domain.project.entity.Project;
import com.task.weaver.domain.member.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.member.user.dto.request.RequestGetUserPage;
import com.task.weaver.domain.member.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.member.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.member.user.entity.User;
import com.task.weaver.domain.useroauthmember.dto.response.ResponseUserOauth;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ResponseUuid getUuid(String email, Boolean checked);

    ResponseGetUser getUser(UUID userId);

    ResponseUserIdNickname getUser(String email, Boolean checked);

    ResponseGetUserForFront getUserFromToken(HttpServletRequest request);

    ResponseUserMypage getUserInfo(String userId);

    ResponsePageResult<ResponseGetUserList, User> getUsers(RequestGetUserPage requestGetUserPage);
    ResponseUserOauth.AllMember getUsersForTest();
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
