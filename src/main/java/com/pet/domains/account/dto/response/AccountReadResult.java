package com.pet.domains.account.dto.response;

import lombok.Getter;

@Getter
public class AccountReadResult {

    private final Long id;

    private final String nickname;

    private final String email;

    public AccountReadResult(Long id, String nickname, String email) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
    }

}
