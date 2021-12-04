package com.pet.domains.comment.dto.response;

import lombok.Getter;

@Getter
public class CommentUpdateResult {

    private final Long id;

    private CommentUpdateResult(Long id) {
        this.id = id;
    }

    public static CommentUpdateResult of(Long id) {
        return new CommentUpdateResult(id);
    }
}
