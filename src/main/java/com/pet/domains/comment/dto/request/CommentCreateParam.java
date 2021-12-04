package com.pet.domains.comment.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentCreateParam {

    private Long postId;

    private String content;

    private Long parentCommentId;

    private CommentCreateParam(Long postId, String content, Long parentCommentId) {
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }

    public static CommentCreateParam of(Long postId, String content, Long parentCommentId) {
        return new CommentCreateParam(postId, content, parentCommentId);
    }

    public static CommentCreateParam of(Long postId, String content) {
        return new CommentCreateParam(postId, content, null);
    }
}
