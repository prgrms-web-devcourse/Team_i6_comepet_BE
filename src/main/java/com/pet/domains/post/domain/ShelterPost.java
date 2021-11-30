package com.pet.domains.post.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shelter_post")
public class ShelterPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "age", columnDefinition = "SMALLINT", nullable = false)
    private int age;

    @Column(name = "shelter_place", columnDefinition = "VARCHAR(200)", nullable = false)
    private String shelterPlace;

    @Column(name = "shelter_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String shelterName;

    @Column(name = "shelter_tel_number", columnDefinition = "VARCHAR(14)", nullable = false)
    private String shelterTelNumber;

    @Column(name = "manager", columnDefinition = "VARCHAR(20)", nullable = false)
    private String manager;

    @Column(name = "color", columnDefinition = "VARCHAR(30)", nullable = false)
    private String color;

    @Column(name = "desertion_number", columnDefinition = "VARCHAR(20)", nullable = false)
    private String desertionNumber;

    @Column(name = "image", columnDefinition = "VARCHAR(255)", nullable = false)
    private String image;

    @Column(name = "found_date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime foundDate;

    @Column(name = "found_place", columnDefinition = "VARCHAR(200)", nullable = false)
    private String foundPlace;

    @Enumerated(EnumType.STRING)
    @Column(name = "neutered", columnDefinition = "VARCHAR(10)", nullable = false)
    private NeuteredType neutered;

    @Column(name = "start_date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", columnDefinition = "DATE", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "manager_tel_number", columnDefinition = "VARCHAR(14)", nullable = false)
    private String managerTelNumber;

    @Column(name = "post_status", columnDefinition = "VARCHAR(10)", nullable = false)
    private String postStatus;

    @Column(name = "sex", columnDefinition = "VARCHAR(10)", nullable = false)
    private ShelterSexType sex;

    @Column(name = "feature", columnDefinition = "VARCHAR(200)", nullable = false)
    private String feature;

    @Column(name = "weight", columnDefinition = "DECIMAL", nullable = false)
    private BigDecimal weight;

    @Column(name = "notice_number", columnDefinition = "VARCHAR(30)", nullable = false)
    private String noticeNumber;

    //시군구

    //품종

}
