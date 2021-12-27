package com.pet.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class Assertions {

    public static void assertThrow(boolean condition, RuntimeException exception) {
        if (condition) {
            log.debug(exception.getMessage());
            throw exception;
        }
    }
}
