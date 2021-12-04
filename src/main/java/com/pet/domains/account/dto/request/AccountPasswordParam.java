package com.pet.domains.account.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountPasswordParam {

    private String password;

    public AccountPasswordParam(String password) {
        this.password = password;
    }

}
