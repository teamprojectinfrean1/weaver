package com.task.weaver.common.config;

import com.task.weaver.domain.issue.util.IssueStatusTypeConverter;
import com.task.weaver.domain.userOauthMember.oauth.util.OauthServerTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders("*");
    }

    /**
     * converter 설정 추가
     */
    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new OauthServerTypeConverter());
        registry.addConverter(new IssueStatusTypeConverter());
    }
}
