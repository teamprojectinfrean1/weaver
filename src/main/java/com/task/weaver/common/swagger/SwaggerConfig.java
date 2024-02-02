package com.task.weaver.common.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@SecurityScheme(type = SecuritySchemeType.APIKEY, name = "Access-Token", in = SecuritySchemeIn.HEADER)
//@OpenAPIDefinition(security = {@SecurityRequirement(name = "Access-Token")})
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi publicMemberApi() {
        return GroupedOpenApi.builder()
                .group("PROJECT")
                .pathsToMatch("/api/v1/project/**")
                .build();
    }

    @Bean
    public OpenAPI weaverOpenAPI() {
        String title = "TEAM WEAVER API DOCS";
        String description = "WEAVER API end-point Description";
        String version  = "1.0.1";

        return new OpenAPI().addServersItem(new Server().url("/"))
                .components(new Components())
                .info(new Info()
                        .title(title)
                        .description(description)
                        .version(version)
                        .license(new License().name("Apache 2.0").url("<http://springdoc.org>")));
    }
}