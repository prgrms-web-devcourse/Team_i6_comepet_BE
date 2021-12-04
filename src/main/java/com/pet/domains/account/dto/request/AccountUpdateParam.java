package com.pet.domains.account.dto.request;

import lombok.Getter;

@Getter
public class AccountUpdateParam {

    private Long id;

    private String nickname;

    public AccountUpdateParam(Long id, String name) {
        this.id = id;
        this.nickname = name;
    }

}
