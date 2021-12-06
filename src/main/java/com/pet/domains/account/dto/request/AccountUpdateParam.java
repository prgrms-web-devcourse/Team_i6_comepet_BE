package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountUpdateParam {

    private Long id;

    private String nickname;

    public AccountUpdateParam(Long id, String name) {
        this.id = id;
        this.nickname = name;
    }

}
