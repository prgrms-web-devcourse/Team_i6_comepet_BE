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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shelter_post")
public class ShelterPost extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "age", columnDefinition = "SMALLINT default 0", nullable = false)
    private int age;

    @Column(name = "shelter_place", length = 200, nullable = false)
    private String shelterPlace;

    @Column(name = "shelter_name", length = 50, nullable = false)
    private String shelterName;

    @Column(name = "shelter_tel_number", length = 14, nullable = false)
    private String shelterTelNumber;

    @Column(name = "manager", length = 20, nullable = false)
    private String manager;

    @Column(name = "color", length = 30, nullable = false)
    private String color;

    @Column(name = "desertion_number", length = 20, nullable = false)
    private String desertionNumber;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "found_date", nullable = false)
    private LocalDate foundDate;

    @Column(name = "found_place", length = 200, nullable = false)
    private String foundPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "neutered", length = 10, nullable = false)
    private NeuteredType neutered;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "manager_tel_number", length = 14, nullable = false)
    private String managerTelNumber;

    @Column(name = "post_status", length = 10, nullable = false)
    private String postStatus;

    @Column(name = "sex", length = 10, nullable = false)
    private ShelterSexType sex;

    @Column(name = "feature", length = 200, nullable = false)
    private String feature;

    @Column(name = "weight", columnDefinition = "DOUBLE default 0.0", nullable = false)
    private Double weight;

    @Column(name = "notice_number", length = 30, nullable = false)
    private String noticeNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "town_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_town_to_shelter_post"))
    private Town town;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_kind_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_animal_kind_to_shelter_post"))
    private AnimalKind animalKind;

}
