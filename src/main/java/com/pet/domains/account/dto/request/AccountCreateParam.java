package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountCreateParam {

    private String nickname;

    private String email;

    private String password;

    private MultipartFile file;

    @Builder
    public AccountCreateParam(String nickname, String email, String password,
        MultipartFile file) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.file = file;
    }
}
