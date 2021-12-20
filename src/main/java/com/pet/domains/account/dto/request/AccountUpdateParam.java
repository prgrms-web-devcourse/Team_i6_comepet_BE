package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountUpdateParam {

    private String nickname;

    private String newPassword;

    private String newPasswordCheck;

    public AccountUpdateParam(String nickname, String newPassword, String newPasswordCheck) {
        this.nickname = nickname;
        this.newPassword = newPassword;
        this.newPasswordCheck = newPasswordCheck;
    }
}

