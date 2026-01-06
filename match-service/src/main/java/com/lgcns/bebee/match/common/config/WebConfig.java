package com.lgcns.bebee.match.common.config;

import com.lgcns.bebee.common.config.BaseWebConfig;
import com.lgcns.bebee.common.properties.CorsProperties;
import com.lgcns.bebee.common.web.CurrentMemberArgumentResolver;
import com.lgcns.bebee.common.web.MemberAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

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
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/api-docs/**");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(currentMemberArgumentResolver);
    }
}
