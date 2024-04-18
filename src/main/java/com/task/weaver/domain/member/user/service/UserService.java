package com.task.weaver.domain.member.user.service;

import com.task.weaver.common.aop.annotation.LoggingStopWatch;
import com.task.weaver.domain.authorization.dto.request.RequestSignIn;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.member.UserOauthMember;
import com.task.weaver.domain.member.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.member.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.member.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.member.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.member.user.dto.response.ResponseSimpleURL;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.simple.parser.ParseException;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    @LoggingStopWatch
    ResponseToken weaverLogin(RequestSignIn requestSignIn);
    @LoggingStopWatch
    ResponseGetMember addUser(RequestCreateUser requestCreateUser, MultipartFile multipartFile) throws IOException;
    @LoggingStopWatch
    ResponseGetMember updateUser(UUID userId, RequestUpdateUser requestUpdateUser)
            throws IOException, ParseException;
    @LoggingStopWatch
    void updateUser(RequestUpdatePassword requestUpdateUser);
    @LoggingStopWatch
    ResponseSimpleURL updateProfile(MultipartFile multipartFile, UUID memberUUID) throws IOException;
    @LoggingStopWatch
    void deleteUser(UUID userId);
}
