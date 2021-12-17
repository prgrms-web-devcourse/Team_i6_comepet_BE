package com.pet.domains.comment.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class CommentPageResults {

    private List<CommentPageResults.Comment> comments;

    private long totalElements;

    private boolean last;

    private long size;

    public CommentPageResults(List<Comment> comments, long totalElements, boolean last, long size) {
        this.comments = comments;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    @Getter
    public static class Comment {

        private long id;

        private String content;

        private LocalDateTime createdAt;

        private Comment.Account account;

        private List<ChildComment> childComments;

        public Comment(long id, String content, LocalDateTime createdAt,
            Account account,
            List<ChildComment> childComments) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.account = account;
            this.childComments = childComments;
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

            private Comment.Account account;

            public ChildComment(long id, String content, LocalDateTime createdAt,
                Account account) {
                this.id = id;
                this.content = content;
                this.createdAt = createdAt;
                this.account = account;
            }
        }
    }
}
