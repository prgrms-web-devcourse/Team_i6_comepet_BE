package com.pet.domains.area.domain;

import com.pet.domains.BaseEntity;
import com.pet.domains.account.domain.Account;
import java.util.Objects;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "interest_area")
@EqualsAndHashCode(of = "id", callSuper = false)
public class InterestArea extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "selected", nullable = false, columnDefinition = "boolean default false")
    private boolean selected;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "account_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_interest_area_to_account")
    )
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "town_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_interest_area_to_town")
    )
    private Town town;

    @Builder
    public InterestArea(boolean selected, Account account, Town town) {
        Validate.notNull(account, "account must not be null");
        Validate.notNull(town, "town must not be null");

        this.selected = selected;
        this.account = account;
        this.town = town;
    }

    public void checkSelect() {
        this.selected = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InterestArea)) {
            return false;
        }
        InterestArea that = (InterestArea)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
