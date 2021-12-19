package com.pet.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class CustomPageableConfiguration {

    @Bean
    public PageableHandlerMethodArgumentResolverCustomizer customize() {
        return p -> p.setFallbackPageable(PageRequest.of(0, 20, Sort.by("id").descending()));
    }
}
