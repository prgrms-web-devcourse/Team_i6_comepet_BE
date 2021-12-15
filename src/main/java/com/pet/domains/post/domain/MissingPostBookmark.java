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
import org.apache.commons.lang3.Validate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(
    name = "missing_post_bookmark",
    uniqueConstraints = @UniqueConstraint(
        name = "uni_missing_post_and_account",
        columnNames = {"missing_post_id", "account_id"}
    )
)
public class MissingPostBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_account_to_missing_book_mark"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missing_post_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_missing_post_to_missing_book_mark"))
    private MissingPost missingPost;

    @Builder
    public MissingPostBookmark(Account account, MissingPost missingPost) {
        Validate.notNull(account, "account must not be null");
        Validate.notNull(missingPost, "missingPost must not be null");

        this.account = account;
        this.missingPost = missingPost;
    }

}
