package com.pet.domains.post.dto.request;

import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
public class MissingPostUpdateParam {

    private Status status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    private Long city;

    private Long town;

    private String detailAddress;

    private String telNumber;

    private Long animal;

    private String animalKindName;

    private int age;

    private SexType sex;

    private String chipNumber;

    private List<PostTag> postTags;

    private String content;

    private List<MultipartFile> files;

    public MissingPostUpdateParam(Status status, LocalDate date, Long city, Long town, String detailAddress,
        String telNumber, Long animal, String animalKindName, int age, SexType sex, String chipNumber,
        List<PostTag> postTags, String content, List<MultipartFile> files) {
        this.status = status;
        this.date = date;
        this.city = city;
        this.town = town;
        this.detailAddress = detailAddress;
        this.telNumber = telNumber;
        this.animal = animal;
        this.animalKindName = animalKindName;
        this.age = age;
        this.sex = sex;
        this.chipNumber = chipNumber;
        this.postTags = postTags;
        this.content = content;
        this.files = files;
    }

    public static MissingPostUpdateParam of(
        Status status, LocalDate date, Long city, Long town, String detailAddress, String telNumber, Long animal,
        String animalKindName, int age, SexType sex, String chipNumber, List<PostTag> postTags, String content,
        List<MultipartFile> files
    ) {
        return new MissingPostUpdateParam(
            status, date, city, town, detailAddress, telNumber, animal, animalKindName, age, sex, chipNumber, postTags,
            content, files
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
