package com.pet.domains;

import com.pet.common.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/exception")
public class EntryExceptionController {

    @GetMapping(value = "/entrypoint")
    public void entrypointException() {
        throw ExceptionMessage.NOT_FOUND_JWT.getException();
    }

}



