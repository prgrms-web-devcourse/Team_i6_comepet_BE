package com.pet.common.exception;

import com.pet.common.exception.httpexception.AuthenticationException;
import com.pet.common.exception.httpexception.BadRequestException;
import com.pet.common.exception.httpexception.ConflictException;
import com.pet.common.exception.httpexception.ForbiddenException;
import com.pet.common.exception.httpexception.InternalServerException;
import com.pet.common.exception.httpexception.NotFoundException;
import com.pet.common.response.ErrorResponse;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAuthenticationException(BadRequestException exception) {
        log.warn("BadRequestException {}", exception.getMessage(), exception);
        return ErrorResponse.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.warn("MethodArgumentNotValidException {}", exception.getMessage(), exception);
        BindingResult result = exception.getBindingResult();
        return ErrorResponse.error(HttpStatus.BAD_REQUEST.value(), result.getFieldErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.toList()));
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindException(BindException exception) {
        log.warn("BindException {}", exception.getMessage(), exception);
        return ErrorResponse.error(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException exception) {
        log.warn("NotFoundException {}", exception.getMessage(), exception);
        return ErrorResponse.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(
        HttpRequestMethodNotSupportedException exception) {
        log.warn("HttpRequestMethodNotSupportedException {}", exception.getMessage(), exception);
        return ErrorResponse.error(HttpStatus.METHOD_NOT_ALLOWED.value(), exception.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(AuthenticationException exception) {
        log.warn("AuthenticationException {}", exception.getMessage(), exception);
        return ErrorResponse.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleForbiddenException(ForbiddenException exception) {
        log.warn("ForbiddenException {}", exception.getMessage(), exception);
        return ErrorResponse.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(ConflictException exception) {
        log.warn("ConflictException {}", exception.getMessage(), exception);
        return ErrorResponse.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler({
        MissingServletRequestParameterException.class, HttpMessageNotReadableException.class,
        TypeMismatchException.class, MultipartException.class, IllegalStateException.class,
        IllegalArgumentException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(Exception exception) {
        log.warn("handleBadRequestException {}", exception.getMessage(), exception);
        return ErrorResponse.error(400, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleConstraintViolation(ConstraintViolationException exception) {
        log.warn("ConstraintViolationException {}", exception.getMessage(), exception);
        return ErrorResponse.error(400, exception.getMessage());
    }

    @ExceptionHandler(HttpMediaTypeException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErrorResponse handleHttpMediaTypeException(HttpMediaTypeException exception) {
        log.warn("HttpMediaTypeException {}", exception.getMessage(), exception);
        return ErrorResponse.error(415, exception.getMessage());
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ErrorResponse handleInternalServerException(InternalServerException exception) {
        log.warn("InternalServerException {}", exception.getMessage(), exception);
        return ErrorResponse.error(exception.getCode(), exception.getMessage());
    }

    @ExceptionHandler(DataAccessException.class)
    public ErrorResponse handleDataAccessException(DataAccessException exception) {
        log.warn("DataAccessException {}", exception.getMessage(), exception);
        return ErrorResponse.error(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(Exception exception) {
        log.error("Exception/RuntimeException {}", exception.getMessage(), exception);
        return ErrorResponse.error(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
    }
}
