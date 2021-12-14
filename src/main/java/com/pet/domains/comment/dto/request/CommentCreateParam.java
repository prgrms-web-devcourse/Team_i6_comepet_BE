package com.pet.domains.comment.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateParam {

    @NotNull(message = "게시글 아이디는 필수입니다.")
    private Long postId;

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    private Long parentCommentId;

    @Builder
    public CommentCreateParam(Long postId, String content, Long parentCommentId) {
        this.postId = postId;
        this.content = content;
        this.parentCommentId = parentCommentId;
    }
}
