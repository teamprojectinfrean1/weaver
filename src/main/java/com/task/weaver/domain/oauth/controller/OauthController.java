package com.task.weaver.domain.oauth.controller;

import com.task.weaver.common.model.OauthServerType;
import com.task.weaver.common.response.DataResponse;
import com.task.weaver.domain.authorization.dto.response.ResponseToken;
import com.task.weaver.domain.authorization.service.AuthorizationService;
import com.task.weaver.domain.authorization.service.impl.AuthorizationServiceImpl;
import com.task.weaver.domain.oauth.entity.OauthMember;
import com.task.weaver.domain.oauth.service.OauthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
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

    /**
     *
     * @param oauthServerType OAuth 제공 서버 kakao.. naver..
     * @param response Service 로직을 통해 생성된 URL로 사용자 Redirect
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
    ResponseEntity<DataResponse<ResponseToken>> login(@PathVariable OauthServerType oauthServerType, @RequestParam("code") String code) {
        log.info("Kakao OAuth --> 로그인 요청 성공");
        ResponseToken responseToken = authorizationService.authenticationOAuthUser(oauthService.login(oauthServerType, code));
        return ResponseEntity.ok(DataResponse.of(HttpStatus.OK, "소셜 로그인 성공", responseToken, true));
    }
}
