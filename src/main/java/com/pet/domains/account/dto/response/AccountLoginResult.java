package com.pet.domains.account.dto.response;

import lombok.Getter;

@Getter
public class AccountLoginResult {

    private Long id;

    private String token;

    public AccountLoginResult(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public static AccountLoginResult of(Long id, String token) {
        return new AccountLoginResult(id, token);
    }

}
