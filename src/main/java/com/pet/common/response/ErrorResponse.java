package com.pet.common.response;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final Object message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse error(String message) {
        return new ErrorResponse(message);
    }

}
