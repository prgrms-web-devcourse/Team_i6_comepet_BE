package com.pet.common.response;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private int code;

    private final Object message;

    public ErrorResponse(int code, Object message) {
        this.code = code;
        this.message = message;
    }

    public static ErrorResponse error(int code, Object message) {
        return new ErrorResponse(code, message);
    }

}
