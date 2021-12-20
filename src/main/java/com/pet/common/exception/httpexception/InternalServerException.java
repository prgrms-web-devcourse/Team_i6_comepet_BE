package com.pet.common.exception.httpexception;

public class InternalServerException extends BaseHttpException {

    public InternalServerException(String message, int code) {
        super(message, code);
    }

}
