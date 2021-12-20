package com.pet.domains.area.domain;

import com.pet.domains.BaseEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.Validate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "city")
public class City extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "name", nullable = false, length = 200, unique = true)
    private String name;

    @OneToMany(mappedBy = "city")
    private List<Town> towns = new ArrayList<>();

    @Builder
    public City(String code, String name) {
        Validate.notBlank(code, "code must not be blank");
        Validate.notBlank(name, "code must not be blank");

        this.code = code;
        this.name = name;
    }
}
