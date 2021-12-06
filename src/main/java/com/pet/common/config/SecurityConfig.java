package com.pet.common.config;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.http.MediaType;

@Configuration
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ACCESS_DINED = "ACCESS_DENIED";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            // .antMatchers("/api/user/me").hasAnyRole("USER", "ADMIN")
            .anyRequest().permitAll()
            .and()

            .formLogin()
            .disable()

            .csrf()
            .disable()

            .headers()
            .disable()

            .httpBasic()
            .disable()

            .rememberMe()
            .disable()

            .logout()
            .disable()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()

            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler())
            .and()
        /**
         * JwtSecurityContextRepository 설정
         */
        // .securityContext()
        // .securityContextRepository(securityContextRepository())
        // .and()
        /**
         * jwtAuthenticationFilter 추가
         */
        //.addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
        ;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = isNotNullPrincipal(authentication);
            log.warn("{} is denied", principal, exception);
            generateResponse(response);
        };
    }

    private Object isNotNullPrincipal(Authentication authentication) {
        return Objects.nonNull(authentication) ? authentication.getPrincipal() : null;
    }

    private void generateResponse(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(ACCESS_DINED);
        response.getWriter().flush();
        response.getWriter().close();
    }

}