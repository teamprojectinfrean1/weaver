package com.task.weaver.domain.member.user.service;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.member.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.member.user.dto.request.RequestUpdateUser;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ResponseToken weaverLogin(RequestSignIn requestSignIn);
    ResponseGetMember addUser(RequestCreateUser requestCreateUser, MultipartFile multipartFile) throws IOException;
    ResponseGetMember updateUser(UUID userId, RequestUpdateUser requestUpdateUser)
            throws IOException, ParseException;
    void updateUser(RequestUpdatePassword requestUpdateUser);
    void deleteUser(UUID userId);
}
