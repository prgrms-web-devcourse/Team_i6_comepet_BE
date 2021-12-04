package com.pet.domains.post.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SexType {
    MALE,
    FEMALE,
    UNKNOWN;

    @JsonCreator
    public static SexType validateSexType(String sex) {
        for (SexType sexType : SexType.values()) {
            if (sexType.name().equals(sex)) {
                return sexType;
            }
        }
        throw new IllegalArgumentException("It is not a value corresponding to the animal gender of the post.");
    }
}
