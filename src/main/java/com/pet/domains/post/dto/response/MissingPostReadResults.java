package com.pet.domains.post.dto.response;

import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class MissingPostReadResults {

    private final List<MissingPostReadResults.MissingPost> missingPosts;

    private final long totalElements;

    private final boolean last;

    private final long size;

    public MissingPostReadResults(
        List<MissingPostReadResults.MissingPost> missingPosts, long totalElements, boolean last, long size
    ) {
        this.missingPosts = missingPosts;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    public static MissingPostReadResults of(
        List<MissingPostReadResults.MissingPost> missingPosts, long totalElements, boolean last, long size
    ) {
        return new MissingPostReadResults(missingPosts, totalElements, last, size);
    }

    @Getter
    public static class MissingPost {

        private final Long id;

        private final String city;

        private final String town;

        private final String animalKind;

        private final Status status;

        private final LocalDateTime createdAt;

        private final SexType sex;

        private final Boolean isBookmark;

        private final long bookmarkCount;

        private final String thumbnail;

        private final List<PostTag> postTags;

        public MissingPost(
            Long id, String city, String town, String animalKind, Status status, LocalDateTime createdAt,
            SexType sex, boolean isBookmark, long bookmarkCount, String thumbnail, List<PostTag> postTag
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
            this.postTags = postTag;
        }

        public static MissingPost of(
            Long id, String city, String town, String animalKind, Status status, LocalDateTime createdAt,
            SexType sex, boolean isBookmark, long bookmarkCount, String thumbnail, List<PostTag> postTag
        ) {
            return new MissingPost(
                id, city, town, animalKind, status, createdAt, sex, isBookmark, bookmarkCount, thumbnail, postTag
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
