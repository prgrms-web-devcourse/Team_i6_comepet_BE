package com.pet.common.exception;

/**
 * 정책 정하기전에 우선 필요해서 만들었습니다.
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

}
