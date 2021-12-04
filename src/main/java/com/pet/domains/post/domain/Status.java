package com.pet.domains.post.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    MISSING,
    DETECTION,
    PROTECTION,
    COMPLETION;

    @JsonCreator
    public static Status validateStatus(String status) {
        for (Status statusType : Status.values()) {
            if (statusType.name().equals(status)) {
                return statusType;
            }
        }
        throw new IllegalArgumentException("This value does not correspond to the status of the post.");
    }
}
