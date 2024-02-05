package com.task.weaver.domain.oauth.dto;

public interface SocialToken {
	Long getId();
	String getEmail();
	String getNickname();
	String getImageSrc();

	default SocialToken type() {
		if(this instanceof KakaoOAuth2)
			return SocialToken.KAKAO;
		else
			return null;
	}
}
