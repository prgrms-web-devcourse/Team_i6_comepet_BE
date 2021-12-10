package com.pet.domains.post.dto.request;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MissingPostCreateParam {

    private String status;

    private LocalDate date;

    private Long cityId;

    private Long townId;

    private String detailAddress;

    private String telNumber;

    private Long animalId;

    private String animalKindName;

    private int age;

    private String sex;

    private String chipNumber;

    private String content;

    private List<MissingPostCreateParam.Tag> tags;

    public MissingPostCreateParam(String status, LocalDate date, Long cityId, Long townId, String detailAddress,
        String telNumber, Long animalId, String animalKindName, int age, String sex, String chipNumber,
        String content, List<Tag> tags) {
        this.status = status;
        this.date = date;
        this.cityId = cityId;
        this.townId = townId;
        this.detailAddress = detailAddress;
        this.telNumber = telNumber;
        this.animalId = animalId;
        this.animalKindName = animalKindName;
        this.age = age;
        this.sex = sex;
        this.chipNumber = chipNumber;
        this.content = content;
        this.tags = tags;
    }

    public static MissingPostCreateParam of(
        String status, LocalDate date, Long cityId, Long townId, String detailAddress,
        String telNumber, Long animalId, String animalKindName, int age, String sex, String chipNumber,
        String content, List<Tag> tags
    ) {
        return new MissingPostCreateParam(
            status, date, cityId, townId, detailAddress, telNumber, animalId, animalKindName, age, sex, chipNumber,
            content, tags
        );
    }

    @Getter
    @NoArgsConstructor
    public static class Tag {

        private String name;

        public Tag(String name) {
            this.name = name;
        }

        public static Tag of(String name) {
            return new Tag(name);
        }

    }

}
