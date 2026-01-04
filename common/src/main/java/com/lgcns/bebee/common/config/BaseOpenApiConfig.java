package com.lgcns.bebee.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class BaseOpenApiConfig {

    protected Components openApiComponents(){
        return new Components()
                .addSecuritySchemes("bearerAuth", new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT 인증 토큰을 입력하세요"))
                .addSecuritySchemes("memberIdHeader", new SecurityScheme()
                        .type(SecurityScheme.Type.APIKEY)
                        .in(SecurityScheme.In.HEADER)
                        .name("X-Member-Id")
                        .description("회원 ID를 입력하세요 (테스트용)"));
    }

    /**
     * 전역 Security Requirement 설정
     * 모든 API에 bearerAuth와 memberIdHeader를 적용합니다.
     */
    protected List<SecurityRequirement> openApiSecurityRequirements(){
        return List.of(
                new SecurityRequirement()
                        .addList("bearerAuth")
                        .addList("memberIdHeader")
        );
    }

    /**
     * 기본 OpenAPI 설정 - 각 서비스에서 오버라이드 가능
     */
    protected Info createApiInfo(String title, String description, String version){
        return new Info()
                .title(title)
                .description(description)
                .version(version)
                .contact(new Contact()
                        .name("LG CNS Bebee Team")
                );
    }
}
