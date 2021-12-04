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

        private final String AnimalKind;

        private final SexType sexType;

        private final String place;

        private final LocalDate createdAt;

        private final String thumbnail;

        private final int bookmarkCount;

        public Post(
            Long id, String animalKind, SexType sexType, String place, LocalDate createdAt,
            String thumbnail, int bookmarkCount
        ) {
            this.id = id;
            AnimalKind = animalKind;
            this.sexType = sexType;
            this.place = place;
            this.createdAt = createdAt;
            this.thumbnail = thumbnail;
            this.bookmarkCount = bookmarkCount;
        }

        public static Post of(
            Long id, String animalKind, SexType sexType, String shelterPlace, LocalDate createdAt,
            String thumbnail, int bookmarkCount
        ) {
            return new Post(id, animalKind, sexType, shelterPlace, createdAt, thumbnail, bookmarkCount);
        }

    }

}
