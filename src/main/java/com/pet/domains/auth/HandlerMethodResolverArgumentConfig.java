package com.pet.domains.auth;

import com.pet.domains.account.service.LoginService;
import com.pet.domains.auth.controller.argumentresolver.JwtAuthenticationArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class HandlerMethodResolverArgumentConfig implements WebMvcConfigurer {

    private final LoginService loginService;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(jwtAuthenticationArgumentResolver());
    }

    @Bean
    public JwtAuthenticationArgumentResolver jwtAuthenticationArgumentResolver() {
        return new JwtAuthenticationArgumentResolver(loginService);
    }

}
