package com.task.weaver.common.jwt.provider;

import com.task.weaver.domain.member.LoginType;
import io.jsonwebtoken.Jws;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.task.weaver.domain.member.user.service.Impl.CustomUserDetailsService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
	@Value("${jwt.secret.key}")
	private String secretKey;
	private Key key;

	// access token -> 30minutes
	private final Long ACCESS_TOKEN_VAILD_TIME = 30 * 60 * 1000L;
	// refresh token -> 7days
	private final Long REFRESH_TOKEN_VALID_TIME = 7 * 24 * 60 * 60 * 1000L;

	private final CustomUserDetailsService customUserDetailsService;
	private static final String AUTHORIZATION_HEADER = "Authorization";
	/**
	 * 객체 초기화, secretKey를 Base64로 인코딩
	 */
	@PostConstruct
	protected void init() {
		String encoededKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		key = Keys.hmacShaKeyFor(encoededKey.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Access Token 생성
	 */
	public String createAccessToken(Authentication authentication, LoginType loginType){
		Claims claims = Jwts.claims().setSubject(authentication.getName());
		claims.put("loginType", loginType.toString());
		Date now = new Date();
		Date validity = new Date(now.getTime() + ACCESS_TOKEN_VAILD_TIME);

		return Jwts.builder()
			.setClaims(claims) // 이 코드 더 찾아보기 -> 이전 플젝에서는 ResponseDto 받아와서 email 넣어줌
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

		String j = Jwts.builder()
			.setSubject(authentication.getName()) // sub
			.setIssuedAt(now) // 발행 시간
			.signWith(key, SignatureAlgorithm.HS512) // 암호화
			.setExpiration(validity) // 만료 시간
			.compact();

		log.info(j);

		return j;
	}

	/**
	 * token 유효성 + 만료일자 확인
	 * @param token
	 * @return 유효성 여부
	 */
	public boolean validateToken(String token) {
		try {
			Claims claims = parseClaims(token);
			return !claims.getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			log.info("만료된 JWT 토큰입니다.");
		} catch (UnsupportedJwtException e) {
			log.info("지원되지 않는 JWT 토큰입니다.");
		} catch (IllegalStateException e) {
			log.info("JWT 토큰이 잘못되었습니다");
		}
		return false;
	}

	/**
	 * AccessToken 으로 Claim 리턴
	 * @param accessToken
	 * @return Claim
	 */
	public Claims parseClaims(String accessToken) {
		try {
			// log.info();
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

	/**
	 * Access Token 검사 후 얻은 정보로 Authentication 객체 생성
	 * @param acceessToken JWT 토큰
	 * @return 인증 정보
	 */
	public Authentication getAuthentication(String acceessToken) {
		Claims claims = parseClaims(acceessToken);

		// email 넣어줘야하는데 ... 확인해보자ㅏ
		log.info("claims.getId : " + claims.getId() + ", claims.getSubject : " + claims.getSubject());
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(HttpServletRequest request){
		String header = request.getHeader(AUTHORIZATION_HEADER);
		log.info("header : " + header);
		String token = header.substring(7);
		log.info("token : " + token);
		String info = Jwts.parserBuilder().setSigningKey(key).build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();

		return info;
	}

	public LoginType decodeAccessToken(String accessToken) {
		Jws<Claims> claimsJws = Jwts.parserBuilder().build().parseClaimsJws(accessToken);
		Claims body = claimsJws.getBody();
		String loginType = body.get("loginType", String.class);
		return LoginType.fromName(loginType);
	}
}
