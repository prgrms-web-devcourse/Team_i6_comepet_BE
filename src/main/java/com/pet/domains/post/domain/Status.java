package com.pet.domains.post.domain;

import com.pet.domains.EnumType;

public enum Status implements EnumType {
    MISSING("실종"),
    DETECTION("목격"),
    PROTECTION("보호"),
    COMPLETION("완료");

    private final String text;

    Status(String text) {
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
