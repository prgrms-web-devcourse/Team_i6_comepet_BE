package com.pet.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pet.common.response.ErrorResponse;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import static com.pet.common.exception.ExceptionMessage.*;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authEx)
        throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        ExceptionMessage notFoundJwt = NOT_FOUND_JWT;
        response.getWriter().write(objectMapper.writeValueAsString(
            ErrorResponse.error(ExceptionMessage.getCode(notFoundJwt), notFoundJwt.getException().getMessage())
        ));
    }

}
