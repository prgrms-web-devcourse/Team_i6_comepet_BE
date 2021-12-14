package com.pet.common.exception.httpexception;

public class UnauthorizedException extends BaseHttpException {

    public UnauthorizedException(String message, int code) {
        super(message, code);
    }

}
