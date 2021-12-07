package com.pet.common.jwt;

import org.apache.commons.lang3.Validate;

public class JwtAuthentication {

    public final String token;
    public final Long accountId;

    public JwtAuthentication(String token, Long accountId) {
        Validate.notBlank(token, "token must be provided.");
        Validate.notNull(accountId, "username must be provided.");
        this.token = token;
        this.accountId = accountId;
    }

}