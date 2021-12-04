package com.pet.domains.post.dto.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;

@Getter
public class ShelterPostPageResults {

    private final List<ShelterPost> shelters;

    private final long totalElements;

    private final boolean last;

    private final long size;

    public ShelterPostPageResults(List<ShelterPostPageResults.ShelterPost> shelters, long totalElements, boolean last,
        long size) {
        this.shelters = shelters;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    public static ShelterPostPageResults of(List<ShelterPostPageResults.ShelterPost> shelters, long totalElements,
        boolean last, long size) {
        return new ShelterPostPageResults(shelters, totalElements, last, size);
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    public static class ShelterPost {

        private final Long id;

        private final String city;

        private final String town;

        private final Long age;

        private final String image;

        private final String animal;

        private final String animalKind;

        private final LocalDate foundDate;

        private final String shelterPlace;

        private final Boolean isBookmark;

        private final Long bookmarkCount;

        public ShelterPost(Long id, String city, String town, Long age, String image, String animal,
            String animalKind, LocalDate foundDate, String shelterPlace, Boolean isBookmark, Long bookmarkCount) {
            this.id = id;
            this.city = city;
            this.town = town;
            this.age = age;
            this.image = image;
            this.animal = animal;
            this.animalKind = animalKind;
            this.foundDate = foundDate;
            this.shelterPlace = shelterPlace;
            this.isBookmark = isBookmark;
            this.bookmarkCount = bookmarkCount;
        }

        public static ShelterPost of(Long id, String city, String town, Long age, String image, String animal,
            String animalKind, LocalDate foundDate, String shelterPlace, Boolean isBookmark, Long bookmarkCount) {
            return new ShelterPost(id, city, town, age, image, animal, animalKind,
                foundDate, shelterPlace, isBookmark, bookmarkCount);
        }
    }
}
