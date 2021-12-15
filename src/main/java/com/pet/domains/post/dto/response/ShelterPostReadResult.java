package com.pet.domains.post.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ShelterPostReadResult {

    private final Long id;

    private final Long age;

    private final String city;

    private final String town;

    private final String shelterName;

    private final String shelterTelNumber;

    private final String color;

    private final String image;

    private final LocalDate foundDate;

    private final String foundPlace;

    private final String animal;

    private final String animalKind;

    private final String neutered;

    private final LocalDate startDate;

    private final LocalDate endDate;

    private final String noticeNumber;

    private final String managerTelNumber;

    private final String status;

    private final String sex;

    private final String feature;

    private final Double weight;

    private final Boolean isBookmark;

    private final Long bookmarkCount;

    @Builder
    private ShelterPostReadResult(Long id, Long age, String city, String town, String shelterName,
        String shelterTelNumber, String color, String image, LocalDate foundDate, String foundPlace,
        String animal, String animalKind, String neutered, LocalDate startDate, LocalDate endDate,
        String noticeNumber, String managerTelNumber, String status, String sex, String feature, Double weight,
        Boolean isBookmark, Long bookmarkCount) {
        this.id = id;
        this.age = age;
        this.city = city;
        this.town = town;
        this.shelterName = shelterName;
        this.shelterTelNumber = shelterTelNumber;
        this.color = color;
        this.image = image;
        this.foundDate = foundDate;
        this.foundPlace = foundPlace;
        this.animal = animal;
        this.animalKind = animalKind;
        this.neutered = neutered;
        this.startDate = startDate;
        this.endDate = endDate;
        this.noticeNumber = noticeNumber;
        this.managerTelNumber = managerTelNumber;
        this.status = status;
        this.sex = sex;
        this.feature = feature;
        this.weight = weight;
        this.isBookmark = isBookmark;
        this.bookmarkCount = bookmarkCount;
    }


    @JsonProperty("isBookmark")
    public Boolean getBookmark() {
        return isBookmark;
    }
}
