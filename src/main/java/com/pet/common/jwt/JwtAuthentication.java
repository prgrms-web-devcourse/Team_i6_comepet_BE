package com.pet.common.jwt;

import lombok.Getter;
import org.apache.commons.lang3.Validate;

@Getter
public class JwtAuthentication {

    private final String token;
    private final Long accountId;

    public JwtAuthentication(String token, Long accountId) {
        Validate.notBlank(token, "token must be provided.");
        Validate.notNull(accountId, "username must be provided.");
        this.token = token;
        this.accountId = accountId;
    }

}