package com.pet.common.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "refresh")
@Getter
@RequiredArgsConstructor
public class RefreshJwtProperty {

    private final String header;

    private final String issuer;

    private final String clientSecret;

    private final int expirySeconds;
}
