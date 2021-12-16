package com.pet.domains.animal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = { JpaAuditingConfig.class, QuerydslConfig.class }))
@DisplayName("품종 리포지토리 테스트")
class AnimalKindRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnimalKindRepository animalKindRepository;

    @Test
    @DisplayName("이름으로 AnimalKind 조회 테스트")
    void findAnimalKindByNameTest() {
        // given
        Animal animal = Animal.builder()
            .code("1234")
            .name("testAnimal")
            .build();
        entityManager.persist(animal);

        AnimalKind animalKind = AnimalKind.builder()
            .name("animalKindName")
            .animal(animal)
            .build();
        entityManager.persist(animalKind);

        // when
        AnimalKind foundAnimalKind = animalKindRepository.findByName("animalKindName").get();

        // then
        assertThat(foundAnimalKind.getId()).isEqualTo(animalKind.getId());
    }
}
