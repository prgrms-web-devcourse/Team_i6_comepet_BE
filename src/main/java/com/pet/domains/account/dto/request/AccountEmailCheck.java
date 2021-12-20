package com.pet.domains.account.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountEmailCheck {

    @NotBlank(message = "올바른 이메일 형식이 아닙니다.")
    @Pattern(regexp = "\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "인증번호를 입력해주세요.")
    private String key;

    public AccountEmailCheck(String email, String key) {
        this.email = email;
        this.key = key;
    }

}
