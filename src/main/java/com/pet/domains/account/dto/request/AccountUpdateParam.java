package com.pet.domains.account.dto.request;

import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountUpdateParam {

    private String nickname;

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
        message = "올바른 비밀번호 형식이 아닙니다.")
    private String newPassword;

    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
        message = "올바른 비밀번호 형식이 아닙니다.")
    private String newPasswordCheck;

    public AccountUpdateParam(String nickname, String newPassword, String newPasswordCheck) {
        this.nickname = nickname;
        this.newPassword = newPassword;
        this.newPasswordCheck = newPasswordCheck;
    }
}

