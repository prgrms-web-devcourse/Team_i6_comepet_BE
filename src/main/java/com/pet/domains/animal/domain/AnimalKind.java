package com.pet.domains.animal.domain;

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
import org.apache.commons.lang3.ObjectUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "animal_kind")
public class AnimalKind extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "code", length = 6)
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "animal_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_animal_to_animal_kind"),
        nullable = false
    )
    private Animal animal;

    @Builder
    public AnimalKind(String name, String code, Animal animal) {
        ObjectUtils.requireNonEmpty(name, "name must not be null");
        ObjectUtils.requireNonEmpty(animal, "animal must not be null");

        this.name = name;
        this.code = code;
        this.animal = animal;
    }
}
