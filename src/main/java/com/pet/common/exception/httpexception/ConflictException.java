package com.pet.common.exception.httpexception;

public class ConflictException extends BaseHttpException {

    public ConflictException(String message, int code) {
        super(message, code);
    }

}