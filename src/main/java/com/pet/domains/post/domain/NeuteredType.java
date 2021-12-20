package com.pet.domains.post.domain;

import com.pet.domains.EnumType;

public enum NeuteredType implements EnumType {
    Y("Yes"),
    N("No"),
    U("Unknown");

    private final String text;

    NeuteredType(String text) {
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
