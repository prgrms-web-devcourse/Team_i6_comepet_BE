package com.pet.common.exception.httpexception;

public class NotFoundException extends BaseHttpException {

    public NotFoundException(String message, int code) {
        super(message, code);
    }

}
