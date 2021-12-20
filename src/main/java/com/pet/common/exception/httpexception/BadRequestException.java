package com.pet.common.exception.httpexception;

public class BadRequestException extends BaseHttpException {

    public BadRequestException(String message, int code) {
        super(message, code);
    }

}
