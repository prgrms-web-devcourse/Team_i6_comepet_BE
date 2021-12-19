package com.pet.domains.post.domain;

import com.pet.domains.DeletableEntity;
import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.tag.domain.PostTag;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE missing_post SET deleted = true WHERE id=? and version = ?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "missing_post")
public class MissingPost extends DeletableEntity {

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

    @Column(name = "age", columnDefinition = "SMALLINT default 0")
    private Long age;

    @Enumerated(EnumType.STRING)
    @Column(name = "sex_type", length = 30, nullable = false)
    private SexType sexType;

    @Column(name = "chip_number", length = 15)
    private String chipNumber;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "tel_number", length = 15, nullable = false)
    private String telNumber;

    @Column(name = "view_count", columnDefinition = "BIGINT default 0", nullable = false)
    private long viewCount;

    @Formula("(select count(*) from missing_post_bookmark mpb where mpb.missing_post_id = id)")
    private long bookmarkCount;

    @Formula("(select count(*) from comment c where c.missing_post_id = id)")
    private long commentCount;

    @Column(name = "thumbnail")
    private String thumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "account_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_account_to_missing_post"),
        nullable = false
    )
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "town_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_town_to_missing_post"),
        nullable = false
    )
    private Town town;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "animal_kind_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_animal_kind_to_missing_post"),
        nullable = false
    )
    private AnimalKind animalKind;

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "missingPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTags = new ArrayList<>();

    @BatchSize(size = 20)
    @OneToMany(mappedBy = "missingPost", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    @Version
    private long version;

    @Builder
    public MissingPost(Status status, String detailAddress, LocalDate date, Long age,
        SexType sexType, String chipNumber, String content, String telNumber, long viewCount, long bookmarkCount,
        long commentCount, String thumbnail, Account account, Town town, AnimalKind animalKind) {
        Validate.notNull(status, "status must not be null");
        Validate.notNull(date, "date must not be null");
        Validate.notNull(sexType, "sexType must not be null");
        Validate.notBlank(content, "content must not be blank");
        Validate.notBlank(telNumber, "telNumber must not be blank");
        Validate.notNull(account, "account must not be null");
        Validate.notNull(town, "town must not be null");
        Validate.notNull(animalKind, "animalKind must not be null");

        this.status = status;
        this.detailAddress = detailAddress;
        this.date = date;
        this.age = age;
        this.sexType = sexType;
        this.chipNumber = chipNumber;
        this.content = content;
        this.telNumber = telNumber;
        this.viewCount = viewCount;
        this.bookmarkCount = bookmarkCount;
        this.commentCount = commentCount;
        this.thumbnail = thumbnail;
        this.account = account;
        this.town = town;
        this.animalKind = animalKind;
    }

    public void increaseViewCount() {
        this.viewCount += 1;
    }

    public void changeInfo(Status status, LocalDate date, Town town, String detailAddress, String telNumber,
        AnimalKind animalKind, Long age, SexType sex, String chipNumber, String content, String thumbnail) {
        if (ObjectUtils.isNotEmpty(status)) {
            this.status = status;
        }

        if (ObjectUtils.isNotEmpty(date)) {
            this.date = date;
        }

        if (ObjectUtils.isNotEmpty(town)) {
            this.town = town;
        }

        if (StringUtils.isNotBlank(telNumber)) {
            this.telNumber = telNumber;
        }

        if (ObjectUtils.isNotEmpty(animalKind)) {
            this.animalKind = animalKind;
        }

        if (ObjectUtils.isNotEmpty(sex)) {
            this.sexType = sex;
        }

        if (StringUtils.isNotBlank(content)) {
            this.content = content;
        }

        this.age = age;
        this.chipNumber = chipNumber;
        this.detailAddress = detailAddress;
        this.thumbnail = thumbnail;
    }
}
