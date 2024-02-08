package com.task.weaver.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

	public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedOrigins("/*") // 외부에서 들어오는 모든 url 허용 -> 추후 수정
			.allowedMethods(ALLOWED_METHOD_NAMES.split(","))
			.allowedHeaders("*") // 허용되는 헤더 -> 추후 수정
			.exposedHeaders("Authorization") // JWT 로그인 위해 클라이언트에서 Authorization 헤더에 접근 가능하도록
			.allowCredentials(true) // 자격증명 허용 (클라이언트에서 쿠키 받기 위해)
			.maxAge(86400L);
	}
}
