package com.lgcns.bebee.member.core.config;

import com.lgcns.bebee.common.config.BaseWebConfig;
import com.lgcns.bebee.common.properties.CorsProperties;
import com.lgcns.bebee.common.web.CurrentMemberArgumentResolver;
import com.lgcns.bebee.common.web.MemberAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

/**
 * Web MVC 설정
 */
@Configuration
public class WebConfig extends BaseWebConfig {
    private final MemberAuthenticationInterceptor memberAuthenticationInterceptor;
    private final CurrentMemberArgumentResolver currentMemberArgumentResolver;

    @Autowired
    public WebConfig(CorsProperties corsProperties,
            MemberAuthenticationInterceptor memberAuthenticationInterceptor,
            CurrentMemberArgumentResolver currentMemberArgumentResolver) {
        super(corsProperties);
        this.memberAuthenticationInterceptor = memberAuthenticationInterceptor;
        this.currentMemberArgumentResolver = currentMemberArgumentResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(memberAuthenticationInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/signup",
                        "/auth/check-email",
                        "/auth/check-nickname",
                        "/documents/**", "/api/documents/**",
                        "/test/**", "/error",
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberArgumentResolver);
    }
}