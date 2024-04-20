package com.task.weaver.common.config;

import com.task.weaver.domain.userOauthMember.oauth.util.OauthServerTypeConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**TODO: 2024-04-17, 수, 18:11  -JEON
*  CORS: 시큐리티에서도 CORS 설정을 다루고 있음(중복) 불필요한 설정인지 확인한 후 삭제할 것
*/
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .exposedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * converter 설정 추가
     */
    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(new OauthServerTypeConverter());
    }
}
