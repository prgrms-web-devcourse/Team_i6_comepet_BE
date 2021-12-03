package com.pet.domains.account.dto.response;

import lombok.Getter;

@Getter
public class AccountCreateResult {

    private Long id;

    private String token;

    public AccountCreateResult(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    public static AccountCreateResult of(Long id, String token) {
        return new AccountCreateResult(id, token);
    }

}
