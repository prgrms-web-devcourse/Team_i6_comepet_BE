package com.pet.domains.account.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class AccountMissingPostPageResults {

    private final List<Post> posts;

    private final long totalElements;

    private final boolean last;

    private final int size;

    public AccountMissingPostPageResults(List<Post> posts, long totalElements, boolean last, int size) {
        this.posts = posts;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    public static AccountMissingPostPageResults of(List<Post> posts, long totalElements, boolean last, int size) {
        return new AccountMissingPostPageResults(posts, totalElements, last, size);
    }

    @Getter
    public static class Post {

        private final Long id;

        private final String city;

        private final String town;

        private final String animalKind;

        private final Status status;

        private final LocalDate date;

        private final SexType sex;

        private final boolean isBookmark;

        private final int bookmarkCount;

        private final List<Post.Tag> postTags;

        private final String thumbnail;

        public Post(Long id, String city, String town, String animalKind, Status status, LocalDate date,
            SexType sex, boolean isBookmark, int bookmarkCount, List<Tag> postTags, String thumbnail) {
            this.id = id;
            this.city = city;
            this.town = town;
            this.animalKind = animalKind;
            this.status = status;
            this.date = date;
            this.sex = sex;
            this.isBookmark = isBookmark;
            this.bookmarkCount = bookmarkCount;
            this.postTags = postTags;
            this.thumbnail = thumbnail;
        }

        public static AccountMissingPostPageResults.Post of(
            Long id, String city, String town, String animalKindId, Status status, LocalDate date, SexType sex,
            boolean isBookmark, int bookmarkCount, List<Tag> posts, String thumbnail
        ) {
            return new AccountMissingPostPageResults.Post(
                id, city, town, animalKindId, status, date, sex, isBookmark, bookmarkCount, posts, thumbnail
            );
        }

        @JsonProperty("isBookmark")
        public boolean isBookmark() {
            return isBookmark;
        }

        @Getter
        public static class Tag {

            private final Long id;

            private final String name;

            public Tag(Long id, String name) {
                this.id = id;
                this.name = name;
            }

            public static Post.Tag of(Long id, String name) {
                return new Post.Tag(id, name);
            }
        }

    }
}
