package com.pet.domains.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class MissingPostCommentPageResults {

    private final List<MissingPostCommentPageResults.Comment> comments;

    private final long totalElements;

    private final boolean last;

    private final long size;

    private MissingPostCommentPageResults(List<Comment> comments, long totalElements, boolean last, long size) {
        this.comments = comments;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    public static MissingPostCommentPageResults of(List<Comment> comments, long totalElements, boolean last, long size) {
        return new MissingPostCommentPageResults(comments, totalElements, last, size);
    }

    @Getter
    public static class Comment {

        private final Long id;

        private final String content;

        private final LocalDateTime createdAt;

        private final Comment.User user;

        public Comment(Long id, String content, LocalDateTime createdAt, Comment.User user) {
            this.id = id;
            this.content = content;
            this.createdAt = createdAt;
            this.user = user;
        }

        public static Comment of(Long id, String content, LocalDateTime createdAt, Comment.User user) {
            return new Comment(id, content, createdAt, user);
        }

        @Getter
        public static class User {

            private final Long id;

            private final String nickname;

            private final String image;

            private User(Long id, String nickname, String image) {
                this.id = id;
                this.nickname = nickname;
                this.image = image;
            }

            public static User of(Long id, String nickname, String image) {
                return new User(id, nickname, image);
            }
        }
    }
}
