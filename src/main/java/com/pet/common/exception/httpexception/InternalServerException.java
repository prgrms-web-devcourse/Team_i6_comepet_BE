package com.pet.common.exception.httpexception;

public class InternalServerException extends RuntimeException {

    public InternalServerException(String message) {
        super(message);
    }

}
