package com.pet.common.config;

import com.pet.common.jwt.Jwt;
import com.pet.common.jwt.JwtAuthenticationFilter;
import com.pet.common.jwt.JwtAuthenticationProvider;
import com.pet.common.oauth2.OAuth2AuthenticationSuccessHandler;
import com.pet.common.property.JwtProperty;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String ACCESS_DINED = "ACCESS_DENIED";

    private final JwtProperty jwtProperty;

    @Bean
    public Jwt jwt() {
        return new Jwt(
            jwtProperty.getIssuer(),
            jwtProperty.getClientSecret(),
            jwtProperty.getExpirySeconds()
        );
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(AccountService accountService, Jwt jwt) {
        return new JwtAuthenticationProvider(jwt, accountService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureAuthentication(
        AuthenticationManagerBuilder builder, JwtAuthenticationProvider authenticationProvider
    ) {
        builder.authenticationProvider(authenticationProvider);
    }

    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwt());
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oauth2AuthenticationSuccessHandler(
        Jwt jwt, AccountService accountService
    ) {
        return new OAuth2AuthenticationSuccessHandler(jwt, accountService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/api/v1/shelter-posts").hasAnyRole("USER", "ANONYMOUS")
            .anyRequest().permitAll()
            .and()

            .formLogin().disable()
            .csrf().disable()
            .cors().and()
            .headers().disable()
            .httpBasic().disable()
            .rememberMe().disable()
            .logout().disable()

            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

            .oauth2Login()
            .authorizationEndpoint()
            .and()
            .successHandler(getApplicationContext().getBean(OAuth2AuthenticationSuccessHandler.class))
            .and()

            .exceptionHandling()
            .accessDeniedHandler(accessDeniedHandler()).and()

            .exceptionHandling()
            .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)).and()

            .addFilterAfter(jwtAuthenticationFilter(), SecurityContextPersistenceFilter.class);
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, exception) -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = isNotNullPrincipal(authentication);
            if (authentication == null) {
                log.warn("권한이 없습니다. {}", principal);
            }
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
