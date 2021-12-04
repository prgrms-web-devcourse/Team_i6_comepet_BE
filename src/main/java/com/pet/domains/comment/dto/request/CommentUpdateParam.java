package com.pet.domains.comment.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentUpdateParam {

    private String content;

    private CommentUpdateParam(String content) {
        this.content = content;
    }

    public static CommentUpdateParam of(String content) {
        return new CommentUpdateParam(content);
    }
}
