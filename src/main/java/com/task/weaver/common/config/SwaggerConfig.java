package com.task.weaver.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
//@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "Access-Token", in = SecuritySchemeIn.HEADER)
//@OpenAPIDefinition(security = {@SecurityRequirement(name = "Access-Token")})
public class SwaggerConfig {

//    @Bean
//    public GroupedOpenApi publicMemberApi() {
//        return GroupedOpenApi.builder()
//                .group("PROJECT")
//                .pathsToMatch("/api/v1/project/**")
//                .build();
//    }

    @Bean
    public OpenAPI weaverOpenAPI() {
        String title = "TEAM WEAVER API DOCS";
        String description = "WEAVER API end-point Description";
        String version  = "1.0.1";

        // Security 스키마 설정
        SecurityScheme bearerAuth = new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("Authorization")
            .in(SecurityScheme.In.HEADER)
            .name(HttpHeaders.AUTHORIZATION);

        // Security 요청 설정
        SecurityRequirement securityRequirement = new SecurityRequirement()
            .addList("Authorization");

        return new OpenAPI().addServersItem(new Server().url("/"))
            // Security 인증 컴포넌트 설정
                .components(new Components().addSecuritySchemes("Authorization", bearerAuth))
            // API 마다 Sucurity 인증 컴포넌트 설정
            .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License().name("Apache 2.0").url("<http://springdoc.org>")));
    }
}