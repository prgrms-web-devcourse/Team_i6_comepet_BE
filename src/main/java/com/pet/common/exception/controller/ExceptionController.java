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

    /**
     * 개발 단계에서 프론트에게 동적으로 에러 메세지를 전달하기 위해 생성
     */
    @GetMapping
    public List<ErrorResponse> getExceptionMessage() {
        return Arrays.stream(ExceptionMessage.values())
            .map(message -> ErrorResponse.error(ExceptionMessage.getCode(message), message.getException().getMessage()))
            .collect(Collectors.toList());
    }

}
