package com.pet.common.exception.httpexception;

public class ForbiddenException extends BaseHttpException {

    public ForbiddenException(String message, int code) {
        super(message, code);
    }

}
