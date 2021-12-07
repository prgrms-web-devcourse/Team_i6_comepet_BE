package com.pet.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry
    //         .addInterceptor(new JwtAuthenticationInterceptor())
    //         .addPathPatterns("/api/v1/**");
    // }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**").allowedOrigins("*");
    }


}
