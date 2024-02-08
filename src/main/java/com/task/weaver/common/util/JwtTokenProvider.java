package com.task.weaver.common.util;

import java.util.Base64;
import java.util.Date;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.task.weaver.domain.user.service.UserService;

import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	@Value("Taskgram2024!")
	private String SECRET_KEY;

	private final long ACCESS_TOKEN_VALID_TIME = 30 * 60 * 1000L; // 30minutes
	private final long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L; // 1week

	private final UserService userService;

	/*
	Encoding SECRET_KEY (Base64)
	 */
	@PostConstruct
	protected void init() {
		SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	}

	/*
	JWT 생성
	 */
	public String createToken(Authorization authorization){
		// Claims claims = Jwts.claims().setSubject(authorization.get)

		Date now = new Date();
		Date validity = new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME);

		return "";
	}
}
