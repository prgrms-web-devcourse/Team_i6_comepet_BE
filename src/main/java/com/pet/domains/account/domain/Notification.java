package com.pet.domains.account.domain;

import com.pet.domains.DeletableEntity;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE notification SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "notification")
public class Notification extends DeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "checked", nullable = false, columnDefinition = "boolean default false")
    private boolean checked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "account_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_notification_to_account")
    )
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "missing_post_id",
        referencedColumnName = "id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_notification_to_missing_post")
    )
    private MissingPost missingPost;

    @Builder
    public Notification(Account account, MissingPost missingPost) {
        Validate.notNull(account, "account must not be null");
        Validate.notNull(missingPost, "account must not be null");

        this.account = account;
        this.missingPost = missingPost;
    }

    public void checkReadStatus() {
        this.checked = true;
    }
}
