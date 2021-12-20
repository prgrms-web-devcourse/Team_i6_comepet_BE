package com.pet.domains.post.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pet.domains.post.domain.SexType;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShelterPostPageResults {

    private final List<ShelterPostPageResults.ShelterPost> shelters;

    private final long totalElements;

    private final boolean last;

    private final long size;

    private ShelterPostPageResults(List<ShelterPostPageResults.ShelterPost> shelters, long totalElements, boolean last,
        long size) {
        this.shelters = shelters;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    public static ShelterPostPageResults of(
        List<ShelterPostPageResults.ShelterPost> shelters,
        long totalElements,
        boolean last,
        long size
    ) {
        return new ShelterPostPageResults(shelters, totalElements, last, size);
    }

    @Getter
    public static class ShelterPost {

        private final Long id;

        private final String city;

        private final String town;

        private final Long age;

        private final String thumbnail;

        private final String animal;

        private final String animalKindName;

        private final LocalDate foundDate;

        private final SexType sex;

        private final boolean isBookmark;

        private final long bookmarkCount;

        @Builder
        public ShelterPost(Long id, String city, String town, Long age, String thumbnail, String animal,
            String animalKindName, LocalDate foundDate, SexType sex, boolean isBookmark, long bookmarkCount) {
            this.id = id;
            this.city = city;
            this.town = town;
            this.age = age;
            this.thumbnail = thumbnail;
            this.animal = animal;
            this.animalKindName = animalKindName;
            this.foundDate = foundDate;
            this.sex = sex;
            this.isBookmark = isBookmark;
            this.bookmarkCount = bookmarkCount;
        }

        @JsonProperty("isBookmark")
        public boolean getBookmark() {
            return isBookmark;
        }
    }
}
