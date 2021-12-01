package com.pet.domains.bookmark.domain;

import com.pet.domains.BaseEntity;
import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "missing_post_bookmark")
public class MissingPostBookmark extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_account_to_missing_book_mark"))
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "missing_post_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_missing_post_to_missing_book_mark"))
    private MissingPost missingPost;

}
