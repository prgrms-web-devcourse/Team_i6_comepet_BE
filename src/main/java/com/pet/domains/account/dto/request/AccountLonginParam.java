package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountLonginParam {

    private String email;

    private String password;

    public AccountLonginParam(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
