package com.pet.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailValidator {
    private static final String EMAIL_REGEX = "\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b";

    public static void validate(String email) {
        Validate.notBlank(email, "email must not be null");
        if (!email.matches(EMAIL_REGEX)) {
            throw new IllegalArgumentException("invalid email : " + email);
        }
    }
}
