package com.task.weaver.domain.oauth.auth;

import org.springframework.stereotype.Component;

import com.task.weaver.domain.oauth.dto.SocialToken;

@Component
public class KakaoOAuth2 implements SocialOAuth2 {

	// 추후에 매핑
	private String KAKAO_LOGIN_URL;
	private String KAKAO_ID;
	private String KAKAO_REDIRECT_URI;

	// public SocialToken getUserInfo(String authorizedCode) {
	// 	String accessToken = this
	//
	// 	return userInfo;
	// }
}
