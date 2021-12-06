package com.pet.domains.comment.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateParam {

    private Long postId;

    private String content;

    private Long parentCommentId;

    @Builder
    private CommentCreateParam(Long postId, String content, Long parentCommentId) {
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }
}
