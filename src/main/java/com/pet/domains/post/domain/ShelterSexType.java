package com.pet.domains.post.domain;

import com.pet.domains.EnumType;

public enum ShelterSexType implements EnumType {
    M("수컷"),
    F("암컷"),
    Q("모름");

    private final String text;

    ShelterSexType(String text) {
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
