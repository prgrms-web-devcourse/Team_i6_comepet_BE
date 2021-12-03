package com.pet.domains.account.dto.request;

import lombok.Getter;

@Getter
public class AccountLonginParam {

    private String email;

    private String password;

    public AccountLonginParam(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
