package com.pet.common.jwt;

import org.apache.commons.lang3.Validate;

public class JwtAuthentication {

    public final String token;
    public final String username;

    public JwtAuthentication(String token, String username) {
        Validate.notBlank(token, "token must be provided.");
        Validate.notBlank(username, "username must be provided.");
        this.token = token;
        this.username = username;
    }

}