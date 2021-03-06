package com.pet.domains.animal.domain;

import com.pet.domains.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "animal")
public class Animal extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "code", length = 30, nullable = false)
    private String code;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "animal")
    private List<AnimalKind> animalKinds = new ArrayList<>();

    @Builder
    public Animal(String name, String code) {
        ObjectUtils.requireNonEmpty(name, "name must not be null");
        ObjectUtils.requireNonEmpty(code, "code must not be null");

        this.name = name;
        this.code = code;
    }
}
