package com.task.weaver.domain.userOauthMember.user.service;

import com.task.weaver.common.aop.annotation.LoggingStopWatch;
import com.task.weaver.domain.member.dto.request.RequestSignIn;
import com.task.weaver.domain.member.dto.response.ResponseToken;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseGetMember;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestCreateUser;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestUpdatePassword;
import com.task.weaver.domain.userOauthMember.user.dto.request.RequestUpdateUser;
import com.task.weaver.domain.userOauthMember.user.dto.response.ResponseSimpleURL;
import java.io.IOException;
import org.json.simple.parser.ParseException;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    ResponseToken weaverLogin(RequestSignIn requestSignIn);
    ResponseGetMember signup(RequestCreateUser requestCreateUser, MultipartFile multipartFile) throws IOException;
    ResponseGetMember updateUserProfile(UUID userId, RequestUpdateUser requestUpdateUser)
            throws IOException, ParseException;
    void updateUser(RequestUpdatePassword requestUpdateUser);
    ResponseSimpleURL updateProfileImage(MultipartFile multipartFile, UUID memberUUID) throws IOException;
    void deleteUser(UUID userId);
}
