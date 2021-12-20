package com.pet.domains.comment.dto.request;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUpdateParam {

    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;

    @Builder
    private CommentUpdateParam(String content) {
        this.content = content;
    }

}
