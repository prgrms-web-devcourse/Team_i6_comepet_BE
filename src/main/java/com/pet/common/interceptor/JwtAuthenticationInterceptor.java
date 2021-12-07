package com.pet.common.interceptor;

import com.pet.common.jwt.JwtMockToken;
import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class JwtAuthenticationInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.startsWith(token, "Bearer")) {
            if (StringUtils.equals(token, JwtMockToken.MOCK_TOKEN)) {
                log.info("success verify token");
                return true;
            }
            throw new AuthenticationException("invalid token");
        }
        return true;
    }
}
