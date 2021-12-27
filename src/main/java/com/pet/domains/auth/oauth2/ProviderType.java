package com.pet.domains.auth.oauth2;

import com.pet.common.exception.ExceptionMessage;
import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProviderType {
    GOOGLE("google", new GoogleUser("sub", "email", "picture")),
    NAVER("naver", new NaverUser("nickname", "email", "profile_image")),
    KAKAO("kakao", new KakaoUser("nickname", "email", "profile_image"));

    private final String type;

    private final Oauth2User oauth2User;

    public static Oauth2User findProvider(String provider) {
        return Arrays.stream(ProviderType.values())
            .filter(providerType -> providerType.type.equals(provider))
            .findAny()
            .map(providerType -> providerType.oauth2User)
            .orElseThrow(ExceptionMessage.NOT_FOUND_PROVIDER::getException);
    }

}
