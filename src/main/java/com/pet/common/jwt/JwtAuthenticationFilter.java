package com.pet.common.jwt;

import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.*;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.common.exception.ExceptionMessage;
import com.pet.common.response.ErrorResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final int EMPTY = 0;

    private final Jwt jwt;

    @Override
    public void doFilterInternal(
        HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException {
        try {
            if (isNull(SecurityContextHolder.getContext().getAuthentication())) {
                String token = getToken(request.getHeader(HttpHeaders.AUTHORIZATION));
                if (nonNull(token)) {
                    initAuthenticationToJwtAuthentication(request, token);
                }
            }
            chain.doFilter(request, response);
        } catch (TokenExpiredException exception) {
            sendErrorResponse(response, ExceptionMessage.INVALID_JWT_EXPIRY);
        } catch (AlgorithmMismatchException
            | SignatureVerificationException
            | JWTDecodeException
            | InvalidClaimException exception) {
            sendErrorResponse(response, ExceptionMessage.INVALID_JWT);
        } catch (Exception e) {
            sendErrorResponse(response, ExceptionMessage.INTERNAL_SERVER);
        }

    }

    private String getToken(String token) {
        if (isNotEmpty(token) || StringUtils.startsWith(token, "Bearer")) {
            return URLDecoder.decode(token.substring("Bearer ".length()), StandardCharsets.UTF_8);
        }
        return null;
    }

    private void initAuthenticationToJwtAuthentication(HttpServletRequest request, String token) {
        Jwt.Claims claims = verify(token);
        Long accountId = claims.getAccountId();
        List<GrantedAuthority> authorities = getAuthorities(claims);

        if (Objects.nonNull(accountId) && authorities.size() > EMPTY) {
            SecurityContextHolder.getContext()
                .setAuthentication(createJwtAuthenticationToken(request, token, accountId, authorities));
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

    private void sendErrorResponse(HttpServletResponse response, ExceptionMessage message) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(
            ErrorResponse.error(ExceptionMessage.getCode(message), message.getException().getMessage())
        ));
    }
}
