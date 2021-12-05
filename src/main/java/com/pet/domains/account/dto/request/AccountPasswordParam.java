package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountPasswordParam {

    private String password;

    public AccountPasswordParam(String password) {
        this.password = password;
    }

}
