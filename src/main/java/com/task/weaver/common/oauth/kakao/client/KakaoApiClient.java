package com.task.weaver.common.oauth.kakao.client;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.task.weaver.common.oauth.kakao.dto.KakaoMemberResponse;
import com.task.weaver.common.oauth.kakao.dto.KakaoToken;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface KakaoApiClient {

    /**
     * @param params ContentType, 요청 파라미터
     * @return 카카오 토큰 정보
     * @URL: AccessToken을 받아오기 위한 카카오 URL
     * @Configuration: Http Interface Client 구현체를 Bean으로 등록하는 과정 필요<br/> -> HttpInterfaceConfig.class 참조
     */
    @PostExchange(url = "https://kauth.kakao.com/oauth/token", contentType = APPLICATION_FORM_URLENCODED_VALUE)
    KakaoToken fetchToken(@RequestParam MultiValueMap<String, String> params);

    /**
     * @param bearerToken kakao AccessToken
     * @return user information
     * @URL: 유저 정보를 받아오기 위한 카카오 URL
     */
    @GetExchange("https://kapi.kakao.com/v2/user/me")
    KakaoMemberResponse fetchMember(@RequestHeader(name = AUTHORIZATION) String bearerToken);
}

