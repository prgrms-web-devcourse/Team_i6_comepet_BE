package com.pet.common.config;

import com.pet.common.jwt.Jwt;
import com.pet.common.jwt.JwtAuthenticationFilter;
import com.pet.common.jwt.JwtAuthenticationProvider;
import com.pet.domains.account.service.AccountService;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ACCESS_DINED = "ACCESS_DENIED";

    private final JwtConfig jwtConfig;

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

            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and()

            .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class)
        ;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(AccountService accountService, Jwt jwt) {
        return new JwtAuthenticationProvider(jwt, accountService);
    }

    @Autowired
    public void configureAuthentication(AuthenticationManagerBuilder builder, JwtAuthenticationProvider provider) {
        builder.authenticationProvider(provider);
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtConfig.getHeader(), jwt());
    }

    @Bean
    public Jwt jwt() {
        return new Jwt(
            jwtConfig.getIssuer(),
            jwtConfig.getClientSecret(),
            jwtConfig.getExpirySeconds()
        );
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