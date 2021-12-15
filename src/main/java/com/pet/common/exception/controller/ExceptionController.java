package com.pet.common.exception.controller;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.response.ErrorResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/error-info")
public class ExceptionController {

    @GetMapping
    public List<ErrorResponse> getExceptionMessage() {
        return Arrays.stream(ExceptionMessage.values())
            .map(message -> ErrorResponse.error(ExceptionMessage.getCode(message), message.getException().getMessage()))
            .collect(Collectors.toList());
    }

}
