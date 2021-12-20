package com.pet.domains.account.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountSignUpParam {

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$", message = "올바른 닉네임 형식이 아닙니다.")
    private String nickname;

    @NotBlank(message = "올바른 이메일 형식이 아닙니다.")
    @Pattern(regexp = "\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b", message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "올바른 비밀번호 형식이 아닙니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
        message = "올바른 비밀번호 형식이 아닙니다.")
    private String password;

    @NotBlank(message = "올바른 비밀번호 형식이 아닙니다.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$",
        message = "올바른 비밀번호 형식이 아닙니다.")
    private String passwordCheck;

    @NotNull(message = "올바른 요청이 아닙니다.")
    private Long verifiedId;

    public AccountSignUpParam(String nickname, String email, String password, String passwordCheck, Long verifiedId) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.verifiedId = verifiedId;
    }
}
