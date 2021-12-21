package com.pet.domains.account.dto.response;

import com.pet.domains.post.domain.SexType;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class AccountBookmarkPostPageResults {

    private final List<AccountBookmarkPostPageResults.Post> posts;

    private final long totalElements;

    private final boolean last;

    private final long size;

    public AccountBookmarkPostPageResults(
        List<AccountBookmarkPostPageResults.Post> posts, long totalElements, boolean last, long size
    ) {
        this.posts = posts;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    public static AccountBookmarkPostPageResults of(
        List<AccountBookmarkPostPageResults.Post> shelters, long totalElements, boolean last, long size
    ) {
        return new AccountBookmarkPostPageResults(shelters, totalElements, last, size);
    }

    @Getter
    public static class Post {

        private final Long id;

        private final String animalKind;

        private final SexType sexType;

        private final String city;

        private final String town;

        private final LocalDate createdAt;

        private final String thumbnail;

        private final int bookmarkCount;

        public Post(
            Long id, String animalKind, SexType sexType, String city, String town, LocalDate createdAt,
            String thumbnail, int bookmarkCount
        ) {
            this.id = id;
            this.animalKind = animalKind;
            this.sexType = sexType;
            this.city = city;
            this.town = town;
            this.createdAt = createdAt;
            this.thumbnail = thumbnail;
            this.bookmarkCount = bookmarkCount;
        }

        public static Post of(
            Long id, String animalKind, SexType sexType, String city, String town, LocalDate createdAt,
            String thumbnail, int bookmarkCount
        ) {
            return new Post(id, animalKind, sexType, city, town, createdAt, thumbnail, bookmarkCount);
        }

    }

}
