package com.pet.common.exception.httpexception;

public class AuthenticationException extends BaseHttpException {

    public AuthenticationException(String message, int code) {
        super(message, code);
    }

}
