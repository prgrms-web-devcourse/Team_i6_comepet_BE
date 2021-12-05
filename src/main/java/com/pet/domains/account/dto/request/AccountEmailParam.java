package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEmailParam {

    private String email;

    public AccountEmailParam(String email) {
        this.email = email;
    }

}
