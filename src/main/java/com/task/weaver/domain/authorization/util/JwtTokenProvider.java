package com.task.weaver.domain.authorization.util;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.task.weaver.domain.authorization.dto.request.RequestSignIn;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtTokenProvider {
	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;
	@Value("${jwt.expiration.time}")
	public static Integer expirationTime;

	// access token -> 30minutes
	private final Long ACCESS_TOKEN_VAILD_TIME = 30 * 60 * 1000L;
	// refresh token -> 7days
	private final Long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L;

	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String ISSUER = "";

	// private final RedisUtil redisUtil;

	/**
	 * 객체 초기화, secretKey를 Base64로 인코딩
	 */
	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	/**
	 * Access Token 생성
	 */
	public String createAccessToken(Authentication authentication){
		Date now = new Date();
		Date validity = new Date(now.getTime() + ACCESS_TOKEN_VAILD_TIME);

		return Jwts.builder()
			.setSubject(authentication.getName()) // 이 코드 더 찾아보기 -> 이전 플젝에서는 ResponseDto 받아와서 email 넣어줌
			.setIssuedAt(now) // 발행 시간
			.signWith(key, SignatureAlgorithm.HS512) // 암호화
			.setExpiration(validity) // 만료 시간
			.compact();
	}

	/**
	 * Refresh Token 생성
	 */
	public String createRefreshToken(Authentication authentication) {
		Date now = new Date();
		Date validity = new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME);

		return Jwts.builder()
			.setSubject(authentication.getName())
			.setIssuedAt(now) // 발행 시간
			.signWith(key, SignatureAlgorithm.HS512) // 암호화
			.setExpiration(validity) // 만료 시간
			.compact();
	}
}
