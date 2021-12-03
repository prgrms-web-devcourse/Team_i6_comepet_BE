package com.pet.domains.post.dto.request;

import com.pet.domains.tag.domain.PostTag;
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

    private List<PostTag> postTags;

    public MissingPostCreateParam(
        String status, LocalDate date, Long cityId, Long townId, String detailAddress, String telNumber, Long animalId,
        Long animalKindId, int age, String sex, String chipNumber, String content, List<PostTag> postTags
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

}
