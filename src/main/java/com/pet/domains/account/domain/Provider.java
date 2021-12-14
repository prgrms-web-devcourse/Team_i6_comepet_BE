package com.pet.domains.account.domain;

import com.pet.common.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Provider {
    LOCAL("local"),
    GOOGLE("google"),
    KAKAO("kakao");

    private final String type;

    public static Provider findByType(String type) {
        return Arrays.stream(Provider.values())
            .filter(provider -> provider.type.equals(type))
            .findAny()
            .orElseThrow(ExceptionMessage.NOT_FOUND_PROVIDER::getException);
    }

}
