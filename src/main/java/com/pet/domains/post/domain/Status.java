package com.pet.domains.post.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    MISSING("실종"),
    DETECTION("발견"),
    PROTECTION("보호"),
    COMPLETION("완료");

    private final String status;

    public String getMeaning() {
        return this.status;
    }

}
