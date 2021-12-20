package com.pet.common.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "shelter")
public class ShelterProperties {

    private final String name;

    private final String description;

    private final ShelterProperties.Api api;

    @RequiredArgsConstructor
    @Getter
    public static class Api {

        private final String url;
        private final String key;
    }

    public String getUrl() {
        return api.getUrl();
    }

    public String getKey() {
        return api.getKey();
    }

}
