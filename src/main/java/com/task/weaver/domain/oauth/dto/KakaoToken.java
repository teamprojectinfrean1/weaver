package com.task.weaver.domain.oauth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoToken implements SocialToken {
	private Long id;
	private String email;
	private String nickname;
	private String imageSrc;
}
