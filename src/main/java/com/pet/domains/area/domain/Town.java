package com.pet.domains.area.domain;

import com.pet.domains.BaseEntity;
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


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "town")
public class Town extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "code", length = 10)
    private String code;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "city_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_town_to_city")
    )
    private City city;

    @Builder
    public Town(String code, String name, City city) {
        Validate.notBlank(name, "name must not be null");
        Validate.notNull(city, "city must not be null");

        this.code = code;
        this.name = name;
        this.city = city;
    }
}
