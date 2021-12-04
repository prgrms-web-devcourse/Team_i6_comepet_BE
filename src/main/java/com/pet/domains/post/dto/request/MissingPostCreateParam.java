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

    private Long animalKindId;

    private int age;

    private String sex;

    private String chipNumber;

    private String content;

    private List<MissingPostCreateParam.PostTag> postTags;

    public MissingPostCreateParam(
        String status, LocalDate date, Long cityId, Long townId, String detailAddress, String telNumber, Long animalId,
        Long animalKindId, int age, String sex, String chipNumber, String content,
        List<MissingPostCreateParam.PostTag> postTags
    ) {
        this.status = status;
        this.date = date;
        this.cityId = cityId;
        this.townId = townId;
        this.detailAddress = detailAddress;
        this.telNumber = telNumber;
        this.animalId = animalId;
        this.animalKindId = animalKindId;
        this.age = age;
        this.sex = sex;
        this.chipNumber = chipNumber;
        this.content = content;
        this.postTags = postTags;
    }

    public static MissingPostCreateParam of(
        String status, LocalDate date, Long cityId, Long townId, String detailAddress, String telNumber, Long animalId,
        Long animalKindId, int age, String sex, String chipNumber, String content,
        List<MissingPostCreateParam.PostTag> postTags
    ) {
        return new MissingPostCreateParam(
            status, date, cityId, townId, detailAddress, telNumber, animalId, animalKindId, age, sex, chipNumber,
            content, postTags
        );
    }

    @Getter
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
