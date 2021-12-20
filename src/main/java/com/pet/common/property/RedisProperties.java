package com.pet.common.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("spring")
public final class RedisProperties {

    private final Redis redis;

    @Getter
    @RequiredArgsConstructor
    public static final class Redis {

        private final String host;

        private final Integer port;
    }
}
