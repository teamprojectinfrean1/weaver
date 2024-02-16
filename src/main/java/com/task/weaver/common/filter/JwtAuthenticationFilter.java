package com.task.weaver.common.filter;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.task.weaver.domain.authorization.util.JwtTokenProvider;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";

	private final JwtTokenProvider jwtTokenProvider;

	/**
	 * 실제 필터링 로직은 doFilterInternal에 들어감
	 * JWT 토큰의 인증 정보를 현재 스레드의 SecurityContext 에 저장하는 역할 수행
	 * @param request
	 * @param response
	 * @param filterChain
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// 1. Request Header에서 토큰 꺼냄
		String token = parseBearerToken(request);

		try{
			// 2. Validation Access Token
			// validateToken으로 토큰 유효성 검사
			// 정상 토큰이면 해당 토큰으로 Authentication을 가져와 SecurityContext에 저장
			if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
				Authentication authentication = jwtTokenProvider.getAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
				log.info("set Authentication to security context for '{}', uri: {}", authentication.getName(), request.getRequestURI());
			}
		} catch(ExpiredJwtException e){
			request.setAttribute("exception", e);
			log.info("ExpiredJwtException {}", e.getMessage());
		} catch(JwtException | IllegalArgumentException e){
			request.setAttribute("exception", e);
			log.info("jwtException {}", e.getMessage());
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Request Header에서 토큰 정보 꺼내오기기
 * @param request
	 * @return
	 */
	private String parseBearerToken(HttpServletRequest request) {

		String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}

		return null;
	}
}
