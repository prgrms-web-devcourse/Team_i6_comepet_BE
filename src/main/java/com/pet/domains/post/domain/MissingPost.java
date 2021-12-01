package com.pet.domains.post.domain;

import java.time.LocalDate;
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
@Table(name = "missing_post")
public class MissingPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", length = 30, nullable = false)
    private Status status;

    @Column(name = "detail_address")
    private String detailAddress;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "age", columnDefinition = "SMALLINT default 0", nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30, nullable = false)
    private SexType sexType;

    @Column(name = "chip_number", length = 15)
    private String chipNumber;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "tel_number", length = 15, nullable = false)
    private String telNumber;

    @Column(name = "view_count", columnDefinition = "BIGINT default 0", nullable = false)
    private long viewCount;

    @Column(name = "bookmark_count", columnDefinition = "BIGINT default 0", nullable = false)
    private long bookmarkCount;

    @Column(name = "comment_count", columnDefinition = "BIGINT default 0", nullable = false)
    private long commentCount;

    @Column(name = "thumbnail")
    private String thumbnail;

}
