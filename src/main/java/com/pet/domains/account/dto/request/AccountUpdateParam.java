package com.pet.domains.account.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AccountUpdateParam {

    private String nickname;

    private MultipartFile file;

    @Builder
    public AccountUpdateParam(String nickname, MultipartFile file) {
        this.nickname = nickname;
        this.file = file;
    }
}

