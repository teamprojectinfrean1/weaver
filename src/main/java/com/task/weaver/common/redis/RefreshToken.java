package com.task.weaver.common.redis;

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

}
