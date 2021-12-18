package com.pet.domains.post.dto.serach;

import com.pet.domains.post.domain.SexType;
import java.time.LocalDate;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
public class PostSearchParam {

    @Positive
    private Long city;

    @Positive
    private Long town;

    @Positive
    private Long animal;

    @Positive
    private Long animalKind;

    private SexType sex;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    private LocalDate start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end;

    @Builder
    public PostSearchParam(Long city, Long town, Long animal, Long animalKind, SexType sex, LocalDate start,
        LocalDate end) {
        this.city = city;
        this.town = town;
        this.animal = animal;
        this.animalKind = animalKind;
        this.sex = sex;
        this.start = start;
        this.end = end;
    }
}
