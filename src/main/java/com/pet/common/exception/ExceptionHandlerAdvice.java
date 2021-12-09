package com.pet.common.exception;

import com.pet.common.exception.httpexception.BadRequestException;
import com.pet.common.exception.httpexception.NotFoundException;
import com.pet.common.exception.httpexception.UnauthorizedException;
import com.pet.common.response.ErrorResponse;
import javax.naming.AuthenticationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAuthenticationException(BadRequestException e) {
        log.warn(e.getMessage());
        return ErrorResponse.error(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        log.warn(e.getMessage());
        return ErrorResponse.error(e.getMessage());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException e) {
        log.warn(e.getMessage());
        return ErrorResponse.error(e.getMessage());
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAuthenticationException(Exception e) {
        log.error(e.getMessage());
        return ErrorResponse.error(e.getMessage());
    }
}
