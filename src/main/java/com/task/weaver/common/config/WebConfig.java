package com.task.weaver.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")              //와일드 카드 패턴
                .allowedOrigins("*")                          //*로 하면 모든 오리진 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE");  //*로 정하면 모든 메서드 허용
//                .allowedHeaders("*");
//                .maxAge(MAX_AGE_SECS);
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations("file:/Users/ondd/Documents/Java-WorkSpace/weaver-file/generated_files/");
    }
}
