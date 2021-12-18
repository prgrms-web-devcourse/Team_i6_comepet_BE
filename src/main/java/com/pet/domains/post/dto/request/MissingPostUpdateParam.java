package com.pet.domains.post.dto.request;

import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor
public class MissingPostUpdateParam {

    private Status status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Long cityId;

    private Long townId;

    private String detailAddress;

    private String telNumber;

    private Long animal;

    private String animalKindName;

    private Long age;

    private SexType sex;

    private String chipNumber;

    private List<MissingPostUpdateParam.Tag> tags;

    private String content;

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
