package com.pet.domains.post.dto.request;

import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissingPostUpdateParam {

    private Status status;

    private LocalDate date;

    private Long city;

    private Long town;

    private String detailAddress;

    private String telNumber;

    private Long animal;

    private Long animalKind;

    private int age;

    private SexType sex;

    private String chipNumber;

    private List<PostTag> postTags;

    private String content;

    public MissingPostUpdateParam(
        Status status, LocalDate date, Long city, Long town, String detailAddress, String telNumber, Long animal,
        Long animalKind, int age, SexType sex, String chipNumber, List<PostTag> postTags, String content
    ) {
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
        this.postTags = postTags;
        this.content = content;
    }

    public static MissingPostUpdateParam of(
        Status status, LocalDate date, Long city, Long town, String detailAddress, String telNumber, Long animal,
        Long animalKind, int age, SexType sex, String chipNumber, List<PostTag> postTags, String content
    ) {
        return new MissingPostUpdateParam(
            status, date, city, town, detailAddress, telNumber, animal, animalKind, age, sex, chipNumber, postTags,
            content
        );
    }

    @Getter
    @NoArgsConstructor
    public static class PostTag {

        private String name;

        public PostTag(String name) {
            this.name = name;
        }

        public static PostTag of(String name) {
            return new PostTag(name);
        }

    }

}