package com.pet.domains.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class CommentWriteResult {

    private long id;

    private String content;

    private LocalDateTime createdAt;

    private Account account;

    private List<ChildComment> childComments;

    private boolean deleted;

    public CommentWriteResult(long id, String content, LocalDateTime createdAt,
        Account account, List<ChildComment> childComments, boolean deleted) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.account = account;
        this.childComments = childComments;
        this.deleted = deleted;
    }

    @Getter
    public static class Account {

        private long id;

        private String nickname;

        private String image;

        public Account(long id, String nickname, String image) {
            this.id = id;
            this.nickname = nickname;
            this.image = image;
        }
    }

    @Getter
    public static class ChildComment {

        private long id;

        private String content;

        private LocalDateTime createdAt;

        private Account account;

        public ChildComment(long id, String content, LocalDateTime createdAt,
            Account account) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.account = account;
        }
    }
}
