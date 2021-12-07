package com.pet.domains.post.domain;

import com.pet.domains.EnumType;

public enum SexType implements EnumType {
    MALE("수컷"),
    FEMALE("암컷"),
    UNKNOWN("모름");

    private final String text;

    SexType(String text) {
        this.text = text;
    }

    @Override
    public String getName() {
        return this.name();
    }

    @Override
    public String getText() {
        return text;
    }
}
