package com.task.weaver.domain.member.oauth.controller;

import static com.task.weaver.domain.authorization.service.impl.AuthorizationServiceImpl.setCookieAndHeader;

import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.service.impl.AuthorizationServiceImpl;
import com.task.weaver.domain.member.oauth.entity.OauthMember;
import com.task.weaver.domain.member.oauth.service.OauthService;
import com.task.weaver.domain.authorization.entity.UserOauthMember;
import com.task.weaver.domain.authorization.factory.UserOauthMemberFactory;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("api/v1/oauth")
@RestController
public class OauthController {

    private final OauthService oauthService;
    private final AuthorizationServiceImpl authorizationService;
    private final UserOauthMemberFactory userOauthMemberFactory;

    /**
     * @param oauthServerType OAuth 제공 서버 kakao.. naver..
     * @param response        Service 로직을 통해 생성된 URL로 사용자 Redirect
     * @return Void
     */
    @SneakyThrows
    @GetMapping("/{oauthServerType}")
    ResponseEntity<Void> redirectAuthCodeRequestUrl(@PathVariable OauthServerType oauthServerType,
                                                    HttpServletResponse response) {
        log.info("Kakao OAuth --> code 요청 성공");
        String redirectUrl = oauthService.getAuthCodeRequestUrl(oauthServerType);
        response.sendRedirect(redirectUrl);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login/{oauthServerType}")
    ResponseEntity<?> login(@PathVariable OauthServerType oauthServerType,
                                                      @RequestParam("code") String code) {
        log.info("Kakao OAuth --> 로그인 요청 성공");
        OauthMember oauthMember = oauthService.login(oauthServerType, code);
        UserOauthMember userOauthMember = userOauthMemberFactory.createUserOauthMember(oauthMember);
        ResponseToken responseToken = authorizationService.getAuthentication(userOauthMember);
        HttpHeaders headers = setCookieAndHeader(responseToken);
        return new ResponseEntity<>(DataResponse.of(HttpStatus.CREATED, "OAuth login successfully", headers, true), HttpStatus.CREATED);
    }
}
