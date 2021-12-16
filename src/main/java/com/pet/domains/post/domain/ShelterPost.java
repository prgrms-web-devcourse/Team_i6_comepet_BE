package com.pet.domains.post.domain;

import com.pet.domains.BaseEntity;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shelter_post")
public class ShelterPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "age", columnDefinition = "SMALLINT default 0")
    private int age;

    @Column(name = "address", length = 50)
    private String address;

    @Column(name = "shelter_place", length = 200)
    private String shelterPlace;

    @Column(name = "shelter_name", length = 50)
    private String shelterName;

    @Column(name = "shelter_tel_number", length = 14)
    private String shelterTelNumber;

    @Column(name = "manager", length = 20)
    private String manager;

    @Column(name = "color", length = 30)
    private String color;

    @Column(name = "desertion_number", length = 20)
    private String desertionNumber;

    @Column(name = "image")
    private String image;

    @Column(name = "thumbnail")
    private String thumbnail;

    @Column(name = "found_date")
    private LocalDate foundDate;

    @Column(name = "found_place", length = 200)
    private String foundPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "neutered", length = 10)
    private NeuteredType neutered;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "manager_tel_number", length = 14)
    private String managerTelNumber;

    @Column(name = "post_status", length = 10)
    private String postStatus;

    @Column(name = "sex", length = 10)
    private SexType sex;

    @Column(name = "feature", length = 200)
    private String feature;

    @Column(name = "weight", columnDefinition = "DOUBLE default 0")
    private Double weight;

    @Column(name = "notice_number", length = 30)
    private String noticeNumber;

    @Column(name = "bookmark_count", columnDefinition = "BIGINT default 0")
    private long bookmarkCount;

    @Version
    private long version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "town_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_town_to_shelter_post"),
        nullable = false
    )
    private Town town;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "animal_kind_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_animal_kind_to_shelter_post"),
        nullable = false
    )
    private AnimalKind animalKind;

    @Builder
    public ShelterPost(int age, String address, String shelterPlace, String shelterName, String shelterTelNumber,
        String manager, String color, String desertionNumber, String image, String thumbnail, LocalDate foundDate,
        String foundPlace, NeuteredType neutered, LocalDate startDate, LocalDate endDate, String managerTelNumber,
        String postStatus, SexType sex, String feature, Double weight, String noticeNumber, long bookmarkCount,
        Town town, AnimalKind animalKind
    ) {
        Validate.notNull(animalKind, "animalKind must not be null");
        Validate.notNull(town, "town must not be null");

        this.age = age;
        this.address = address;
        this.shelterPlace = shelterPlace;
        this.shelterName = shelterName;
        this.shelterTelNumber = shelterTelNumber;
        this.manager = manager;
        this.color = color;
        this.desertionNumber = desertionNumber;
        this.image = image;
        this.thumbnail = thumbnail;
        this.foundDate = foundDate;
        this.foundPlace = foundPlace;
        this.neutered = neutered;
        this.startDate = startDate;
        this.endDate = endDate;
        this.managerTelNumber = managerTelNumber;
        this.postStatus = postStatus;
        this.sex = sex;
        this.feature = feature;
        this.weight = weight;
        this.noticeNumber = noticeNumber;
        this.bookmarkCount = bookmarkCount;
        this.town = town;
        this.animalKind = animalKind;
    }

    public void increaseBookMarkCount() {
        this.bookmarkCount += 1;
    }

    public void decreaseBookMarkCount() {
        if (this.bookmarkCount != 0) {
            this.bookmarkCount -= 1;
        }
    }

}
