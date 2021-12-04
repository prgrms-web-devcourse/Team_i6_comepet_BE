package com.pet.domains.comment.dto.response;

import lombok.Getter;

@Getter
public class CommentCreateResult {

    private final Long id;

    private CommentCreateResult(Long id) {
        this.id = id;
    }

    public static CommentCreateResult of(Long id) {
        return new CommentCreateResult(id);
    }
}
