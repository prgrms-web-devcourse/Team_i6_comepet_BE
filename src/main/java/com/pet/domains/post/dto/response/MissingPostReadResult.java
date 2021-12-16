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

    private final Account account;

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

    private final List<MissingPostReadResult.Image> images;

    private final List<MissingPostReadResult.Tag> tags;

    private final String content;

    private final long viewCount;

    private final long bookmarkCount;

    private final Boolean isBookmark;

    private final long commentCount;

    private final LocalDateTime createdAt;

    public MissingPostReadResult(
        Long id, Account account, Status status, LocalDate date, String city, String town, String detailAddress,
        String telNumber, String animal, String animalKind, int age, SexType sex, String chipNumber,
        List<Image> images, List<Tag> tags, String content, long viewCount, long bookmarkCount,
        Boolean isBookmark, long commentCount, LocalDateTime createdAt
    ) {
        this.id = id;
        this.account = account;
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
        this.images = images;
        this.tags = tags;
        this.content = content;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.isBookmark = isBookmark;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    public static MissingPostReadResult of(
        Long id, Account account, Status status, LocalDate date, String city, String town, String detailAddress,
        String telNumber, String animal, String animalKind, int age, SexType sex, String chipNumber,
        List<Image> images, List<Tag> tags, String content, long viewCount, long bookmarkCount,
        Boolean isBookmark, long commentCount, LocalDateTime createdAt
    ) {
        return new MissingPostReadResult(
            id, account, status, date, city, town, detailAddress, telNumber, animal, animalKind, age, sex, chipNumber,
            images, tags, content, viewCount, bookmarkCount, isBookmark, commentCount, createdAt
        );
    }

    @Getter
    public static class Account {

        private final Long id;

        private final String nickname;

        private final String image;

        public Account(Long id, String nickname, String image) {
            this.id = id;
            this.nickname = nickname;
            this.image = image;
        }

        public static Account of(Long id, String nickname, String image) {
            return new Account(id, nickname, image);
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
    public static class Tag {

        private final Long id;

        private final String name;

        public Tag(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Tag of(Long id, String name) {
            return new Tag(id, name);
        }

    }

}
