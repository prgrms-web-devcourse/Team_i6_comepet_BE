package com.pet.domains.post.dto.request;

import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import reactor.util.annotation.Nullable;

@Getter
@NoArgsConstructor
public class MissingPostUpdateParam {

    @NotBlank(message = "상태를 선택해주세요.")
    private Status status;

    @NotNull(message = "날짜를 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
    private Long animal;

    @Length(max = 50, message = "범위이내의 품종을 입력해주세요.")
    @NotBlank(message = "품종을 입력해주세요.")
    private String animalKindName;

    @Max(value = 500, message = "범위이내의 나이를 입력해주세요.")
    @NotNull(message = "동물을 입력해주세요.")
    private Long age;

    @NotBlank(message = "성별을 선택해주세요.")
    private SexType sex;

    @Nullable
    @Length(max = 15, message = "범위이내의 칩번호를 입력해주세요.")
    private String chipNumber;

    @Length(max = 255, message = "범위이내의 내용을 입력해주세요.")
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @Nullable
    private List<MissingPostUpdateParam.Tag> tags;

    @Nullable
    private List<MissingPostUpdateParam.Image> images;

    public MissingPostUpdateParam(Status status, LocalDate date, Long cityId, Long townId, String detailAddress,
        String telNumber, Long animal, String animalKindName, Long age, SexType sex, String chipNumber,
        List<Tag> tags, String content, List<Image> images) {
        this.status = status;
        this.date = date;
        this.cityId = cityId;
        this.townId = townId;
        this.detailAddress = detailAddress;
        this.telNumber = telNumber;
        this.animal = animal;
        this.animalKindName = animalKindName;
        this.age = age;
        this.sex = sex;
        this.chipNumber = chipNumber;
        this.tags = tags;
        this.content = content;
        this.images = images;
    }

    public static MissingPostUpdateParam of(
        Status status, LocalDate date, Long cityId, Long townId, String detailAddress, String telNumber, Long animal,
        String animalKindName, Long age, SexType sex, String chipNumber, List<Tag> tags, String content,
        List<Image> images
    ) {
        return new MissingPostUpdateParam(
            status, date, cityId, townId, detailAddress, telNumber, animal, animalKindName, age, sex, chipNumber, tags,
            content, images
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

    @Getter
    @NoArgsConstructor
    public static class Image {

        private Long id;

        private String name;

        public Image(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public static Image of(Long id, String name) {
            return new Image(id, name);
        }

    }

}
