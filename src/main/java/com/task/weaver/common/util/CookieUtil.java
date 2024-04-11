package com.task.weaver.common.util;

import static com.task.weaver.common.jwt.provider.JwtTokenProvider.REFRESH_TOKEN_VALID_TIME;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

/**
 * Cookie 관련 유틸리티 클래스.
 */
public class CookieUtil {

    private CookieUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void setRefreshCookie(HttpHeaders httpHeaders, String refreshToken) {

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .maxAge(REFRESH_TOKEN_VALID_TIME)
                .path("/")
                .sameSite("None")
                .build();

        httpHeaders.add(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
