package com.pet.domains.post.domain;

import com.pet.domains.BaseEntity;
import com.pet.domains.account.domain.Account;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "shelter_post_bookmark",
    uniqueConstraints = @UniqueConstraint(
        name = "uni_shelter_post_and_account",
        columnNames = {"shelter_post_id", "account_id"})
)
public class ShelterPostBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_account_to_shelter_book_mark"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_post_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_shelter_post_to_shelter_book_mark"))
    private ShelterPost shelterPost;

    @Builder
    public ShelterPostBookmark(Account account, ShelterPost shelterPost) {
        ObjectUtils.requireNonEmpty(account, "account must not be null");
        ObjectUtils.requireNonEmpty(shelterPost, "shelterPost must not be null");

        this.account = account;
        this.shelterPost = shelterPost;
    }
}
