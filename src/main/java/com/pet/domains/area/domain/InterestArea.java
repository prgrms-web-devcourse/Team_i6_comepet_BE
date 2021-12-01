package com.pet.domains.area.domain;

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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "interest_area")
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

}
