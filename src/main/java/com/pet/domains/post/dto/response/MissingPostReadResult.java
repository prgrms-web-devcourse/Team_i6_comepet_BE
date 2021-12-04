package com.pet.domains.post.dto.response;

import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class MissingPostReadResult {

    private final Long id;

    private final MissingPostReadResult.User user;

    private final Status status;

    private final LocalDate date;

    private final String city;

    private final String town;

    private final String detailAddress;

    private final String telNumber;

    private final String animal;

    private final String animalKind;

    private final int age;

    private final SexType sex;

    private final String chipNumber;

    private final List<MissingPostReadResult.Image> postImages;

    private final List<MissingPostReadResult.PostTag> postTags;

    private final String content;

    private final long viewCount;

    private final long bookmarkCount;

    private final Boolean isBookmark;

    private final long commentCount;

    private final List<MissingPostReadResult.Comment> comments;

    private final LocalDateTime createdAt;

    public MissingPostReadResult(
        Long id, User user, Status status, LocalDate date, String city, String town, String detailAddress,
        String telNumber, String animal, String animalKind, int age, SexType sex, String chipNumber,
        List<Image> postImages, List<PostTag> postTags, String content, long viewCount, long bookmarkCount,
        Boolean isBookmark, long commentCount, List<Comment> comments, LocalDateTime createdAt
    ) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.date = date;
        this.city = city;
        this.town = town;
        this.detailAddress = detailAddress;
        this.telNumber = telNumber;
        this.animal = animal;
        this.animalKind = animalKind;
        this.age = age;
        this.sex = sex;
        this.chipNumber = chipNumber;
        this.postImages = postImages;
        this.postTags = postTags;
        this.content = content;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.isBookmark = isBookmark;
        this.commentCount = commentCount;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public static MissingPostReadResult of(
        Long id, User user, Status status, LocalDate date, String city, String town, String detailAddress,
        String telNumber, String animal, String animalKind, int age, SexType sex, String chipNumber,
        List<Image> postImages, List<PostTag> postTags, String content, long viewCount, long bookmarkCount,
        Boolean isBookmark, long commentCount, List<Comment> comments, LocalDateTime createdAt
    ) {
        return new MissingPostReadResult(
            id, user, status, date, city, town, detailAddress, telNumber, animal, animalKind, age, sex, chipNumber,
            postImages, postTags, content, viewCount, bookmarkCount, isBookmark, commentCount, comments, createdAt
        );
    }

    @Getter
    public static class User {

        private final Long id;

        private final String nickname;

        private final String image;

        public User(Long id, String nickname, String image) {
            this.id = id;
            this.nickname = nickname;
            this.image = image;
        }

        public static User of(Long id, String nickname, String image) {
            return new User(id, nickname, image);
        }

    }

    @Getter
    public static class Image {

        private final Long id;

        private final String name;

        public Image(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static MissingPostReadResult.Image of(Long id, String name) {
            return new MissingPostReadResult.Image(id, name);
        }

    }

    @Getter
    public static class PostTag {

        private final Long id;

        private final String name;

        public PostTag(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static MissingPostReadResult.PostTag of(Long id, String name) {
            return new MissingPostReadResult.PostTag(id, name);
        }

    }

    @Getter
    public static class Comment {

        private final Long id;

        private final MissingPostReadResult.Comment.User user;

        private final String content;

        private final LocalDateTime createdAt;

        public Comment(Long id, Comment.User user, String content, LocalDateTime createdAt) {
            this.id = id;
            this.user = user;
            this.content = content;
            this.createdAt = createdAt;
        }

        public static Comment of(Long id, Comment.User user, String content, LocalDateTime createdAt) {
            return new Comment(id, user, content, createdAt);
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
