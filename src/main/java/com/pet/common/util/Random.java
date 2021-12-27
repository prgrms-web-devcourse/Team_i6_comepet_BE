package com.pet.common.util;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Random {

    public static String randomNewPassword() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
    }
}
