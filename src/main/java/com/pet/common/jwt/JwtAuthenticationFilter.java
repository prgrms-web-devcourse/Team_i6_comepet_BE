package com.pet.common.jwt;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.*;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.GenericFilterBean;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private static final int EMPTY = 0;

    private final Jwt jwt;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest)request;
        HttpServletResponse servletResponse = (HttpServletResponse)response;

        if (isNull(SecurityContextHolder.getContext().getAuthentication())) {
            String token = getToken(servletRequest.getHeader(HttpHeaders.AUTHORIZATION));
            if (nonNull(token)) {
                initAuthenticationToJwtAuthentication(servletRequest, token);
            }
        } else {
            log.debug("SecurityContextHolder not populated with security token, as it already contained: '{}'",
                SecurityContextHolder.getContext().getAuthentication());
        }
        chain.doFilter(servletRequest, servletResponse);
    }

    private String getToken(String token) {
        if (isNotEmpty(token) || StringUtils.startsWith(token, "Bearer")) {
            log.debug("Jwt authorization api detected: {}", token);
            return URLDecoder.decode(token.substring("Bearer ".length()), StandardCharsets.UTF_8);
        }
        return null;
    }

    private void initAuthenticationToJwtAuthentication(HttpServletRequest request, String token) {
        try {
            Jwt.Claims claims = verify(token);
            log.debug("Jwt parse result: {}", claims);

            Long accountId = claims.getAccountId();
            List<GrantedAuthority> authorities = getAuthorities(claims);

            if (Objects.nonNull(accountId) && authorities.size() > EMPTY) {
                SecurityContextHolder.getContext()
                    .setAuthentication(createJwtAuthenticationToken(request, token, accountId, authorities));
            }
        } catch (Exception exception) {
            log.warn("Jwt processing failed: {}", exception.getMessage());
        }
    }

    private JwtAuthenticationToken createJwtAuthenticationToken(
        HttpServletRequest request, String token, Long accountId, List<GrantedAuthority> authorities
    ) {
        JwtAuthenticationToken authentication = new JwtAuthenticationToken(
            new JwtAuthentication(token, accountId),
            null,
            authorities
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authentication;
    }

    private Jwt.Claims verify(final String token) {
        return jwt.verify(token);
    }

    private List<GrantedAuthority> getAuthorities(Jwt.Claims claims) {
        String[] roles = claims.getRoles();
        if (ArrayUtils.isNotEmpty(roles)) {
            return Arrays.stream(roles).map(SimpleGrantedAuthority::new).collect(toList());
        }
        return Collections.emptyList();
    }

}
