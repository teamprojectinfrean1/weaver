package com.task.weaver.common.redis;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 7)
public class RefreshToken {

	@Id
	private final UUID id;
	@Indexed
	private final String refreshToken;
}
