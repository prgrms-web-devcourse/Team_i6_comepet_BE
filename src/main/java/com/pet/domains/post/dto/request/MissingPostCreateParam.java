package com.pet.domains.post.dto.request;

import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import reactor.util.annotation.Nullable;

@Getter
@NoArgsConstructor
public class MissingPostCreateParam {

    @NotBlank(message = "상태를 선택해주세요.")
    private String status;

    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate date;

    @NotNull(message = "시도를 선택해주세요.")
    private Long cityId;

    @NotNull(message = "시군구를 선택해주세요.")
    private Long townId;

    @Nullable
    @Length(max = 255, message = "범위이내의 상세주소를 입력해주세요.")
    private String detailAddress;

    @Length(max = 15, message = "범위이내의 전화번호를 입력해주세요.")
    @NotBlank(message = "전화번호를 입력해주세요.")
    private String telNumber;

    @NotNull(message = "동물을 선택해주세요.")
    private Long animalId;

    @Length(max = 50, message = "범위이내의 품종을 입력해주세요.")
    @NotBlank(message = "품종을 입력해주세요.")
    private String animalKindName;

    @Max(value = 500, message = "범위이내의 나이를 입력해주세요.")
    @NotNull(message = "동물을 입력해주세요.")
    private Long age;

    @NotBlank(message = "성별을 선택해주세요.")
    private String sex;

    @Nullable
    @Length(max = 15, message = "범위이내의 칩번호를 입력해주세요.")
    private String chipNumber;

    @Length(max = 255, message = "범위이내의 내용을 입력해주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Nullable
    private List<MissingPostCreateParam.Tag> tags;

    public MissingPostCreateParam(String status, LocalDate date, Long cityId, Long townId, String detailAddress,
        String telNumber, Long animalId, String animalKindName, Long age, String sex, String chipNumber,
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
        String telNumber, Long animalId, String animalKindName, Long age, String sex, String chipNumber,
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
