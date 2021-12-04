package com.pet.domains.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountEmailParam {

    private String email;

    public AccountEmailParam(String email) {
        this.email = email;
    }

}
