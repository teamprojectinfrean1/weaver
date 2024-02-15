package com.task.weaver.domain.authorization.redis;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "RefreshToken", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {
	@Id
	private String id;
	private String refreshToken;
	/**
	 * @Indexed 해당 필드 값으로 데이터 찾아옴 -> Key
	 */
	@Indexed
	private String accessToken;
}
