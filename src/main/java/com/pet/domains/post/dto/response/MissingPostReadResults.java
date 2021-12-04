package com.pet.domains.post.dto.response;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class MissingPostReadResults {

    private final List<MissingPostReadResults.MissingPost> missingPosts;

    public MissingPostReadResults(List<MissingPostReadResults.MissingPost> missingPosts) {
        this.missingPosts = missingPosts;
    }

    public static MissingPostReadResults of(List<MissingPostReadResults.MissingPost> missingPosts) {
        return new MissingPostReadResults(missingPosts);
    }

    @Getter
    public static class MissingPost {

        private final Long id;

        private final String city;

        private final String town;

        private final String animalKind;

        private final String status;

        private final LocalDateTime createdAt;

        private final String sex;

        private final boolean isBookmark;

        private final long bookmarkCount;

        private final String thumbnail;

        private final List<PostTag> postTag;

        private final long totalElements;

        private final boolean last;

        private final long size;

        public MissingPost(
            Long id, String city, String town, String animalKind, String status, LocalDateTime createdAt, String sex,
            boolean isBookmark, long bookmarkCount, String thumbnail, List<PostTag> postTag, long totalElements,
            boolean last, long size
        ) {
            this.id = id;
            this.city = city;
            this.town = town;
            this.animalKind = animalKind;
            this.status = status;
            this.createdAt = createdAt;
            this.sex = sex;
            this.isBookmark = isBookmark;
            this.bookmarkCount = bookmarkCount;
            this.thumbnail = thumbnail;
            this.postTag = postTag;
            this.totalElements = totalElements;
            this.last = last;
            this.size = size;
        }

        public static MissingPost of(
            Long id, String city, String town, String animalKind, String status, LocalDateTime createdAt, String sex,
            boolean isBookmark, long bookmarkCount, String thumbnail, List<PostTag> postTag, long totalElements,
            boolean last, long size
        ) {
            return new MissingPost(
                id, city, town, animalKind, status, createdAt, sex, isBookmark, bookmarkCount, thumbnail, postTag,
                totalElements, last, size
            );
        }

        @Getter
        public static class PostTag {

            private final Long id;

            private final String name;

            public PostTag(Long id, String name) {
                this.id = id;
                this.name = name;
            }

            public static PostTag of(Long id, String name) {
                return new PostTag(id, name);
            }

        }

    }

}
