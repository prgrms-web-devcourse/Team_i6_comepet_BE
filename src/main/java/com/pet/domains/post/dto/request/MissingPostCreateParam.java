package com.pet.domains.post.dto.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.util.annotation.Nullable;

@Getter
@NoArgsConstructor
public class MissingPostCreateParam {

    @NotBlank(message = "상태를 선택해주세요.")
    private String status;

    @NotBlank(message = "날짜를 입력해주세요.")
    private LocalDate date;

    @NotBlank(message = "시도를 선택해주세요.")
    private Long cityId;

    @NotBlank(message = "시군구를 선택해주세요.")
    private Long townId;

    private String detailAddress;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String telNumber;

    @Nullable
    private Long animalId;

    @Nullable
    private String animalKindName;

    @Nullable
    private int age;

    @NotBlank(message = "전화번호를 입력해주세요.")
    private String sex;

    @Nullable
    private String chipNumber;

    @Nullable
    private String content;

    @Nullable
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
