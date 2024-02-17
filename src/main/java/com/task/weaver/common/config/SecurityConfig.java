package com.task.weaver.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.task.weaver.common.filter.JwtAuthenticationFilter;
import com.task.weaver.common.handler.JwtAuthenticationEntryPoint;
import com.task.weaver.domain.authorization.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtTokenProvider jwtTokenProvider;
	private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	private static final String[] PERMIT_URL_ARRAY = {
		/* KAKAO */
		// "/user/kakao/callback/**",
		"/api/v1/auth/login",
		"/api/v1/auth/reissue",
		"/api/v1/user/join",
		"/user/logout",
		"/v3/api-docs/**",
		"/swagger-ui.html",
		"/webjars/**",
		"/favicon.com"
	};

	/**
	 * authenticationManager Bean 등록
	 * authenticationManager를 Spring Security 밖으로 빼서 외부에서 사용하기 위함 (로그인 프로세스 커스터마이징)
	 * @param authenticationConfiguration
	 * @return
	 * @throws Exception
	 */
	@Bean
	public AuthenticationManager authenticationManagerBean(
		AuthenticationConfiguration authenticationConfiguration
	) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	/**
	 * WebSecurity 설정
	 * Security 적용하지 않을 리소스 설정
	 * Q. securityFilterChain에서 permitAll로 해주면 안되나?
	 * 		-> WebSecurityCustomizer : FilterChainProxy에 대한 커스터마이징 담당
	 * 		-> SecurityFilterChain : HttpSecurity 구성 담당
	 * @return
	 */
	// web.ignoring()을 사용할 경우 spring security의 보호를 받을 수 없기 때문에 authorizeHttpRequests().permitAll 에 추가하여 설정하는 방식으로 변경
	// @Bean
	// public WebSecurityCustomizer webSecurityCustomizer() {
	// 	return (web) -> web.ignoring()
	// 		.requestMatchers(
	// 			"/v3/api-docs/**",
	// 			"/swagger-ui.html",
	// 			"/webjars/**",
	// 			"/favicon.com"
	// 		);
	// }

	/**
	 * PasswordEncoder
	 * 		: 단방향 해쉬 알고리즘에 Salt 추가하여 Encoding
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	/**
	 * HttpSecurity 설정
	 * WebSecurity에서 제외된 리소스 외의 부분에 Security 설정
	 * @param httpSecurity
	 * @return
	 * @throws Exception
	 */
	@Bean
	protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.csrf(CsrfConfigurer::disable) // rest api이므로 기본 설정 미사용
			.httpBasic(HttpBasicConfigurer::disable) // header에 id, pw가 아닌 jwt 달고감. 따라서 basic 아닌 bearer 사용
			// by default uses a Bean by the name of corsConfigurationSource
			.cors(Customizer.withDefaults())
			.authorizeHttpRequests(authorize -> authorize
				.requestMatchers(PERMIT_URL_ARRAY).permitAll()
				.anyRequest().authenticated())
			// 인증에 관한 예외처리
			.exceptionHandling(handler -> handler
				.authenticationEntryPoint(jwtAuthenticationEntryPoint))
			// JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣는다
			.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
				UsernamePasswordAuthenticationFilter.class)
			// session 미사용
			.sessionManagement(sessionManagement -> sessionManagement
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		return httpSecurity.build();
	}

	/**
	 * CORS 설정
	 * @return
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// 추후 설정
		configuration.addAllowedOriginPattern("*");
		// configuration.addAllowedOrigin("http://localhost:8080");

		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*");
		configuration.setAllowCredentials(true);
		configuration.addExposedHeader("Authorization");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
