package com.pet.common.property;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
@Getter
@AllArgsConstructor
public class JwtProperty {

    private String header;

    private String issuer;

    private String clientSecret;

    private int expirySeconds;

}
