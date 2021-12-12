package com.pet.domains.animal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaAuditingConfig.class))
@DisplayName("AnimalRepository 테스트")
class AnimalRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    @DisplayName("코드로 Animal 조회 테스트")
    void findAnimalByCodeTest() {
        // given
        Animal animal = Animal.builder()
            .code("1234")
            .name("testAnimal")
            .build();
        entityManager.persist(animal);

        // when
        Animal foundAnimal = animalRepository.findByCode("1234").get();

        // then
        assertThat(foundAnimal.getId()).isEqualTo(animal.getId());
    }

    @Test
    @DisplayName("이름로 Animal 조회 테스트")
    void findAnimalByNameTest() {
        // given
        Animal animal = Animal.builder()
            .code("1234")
            .name("testAnimal")
            .build();
        entityManager.persist(animal);

        // when
        Animal foundAnimal = animalRepository.findByName("testAnimal").get();

        // then
        assertThat(foundAnimal.getId()).isEqualTo(animal.getId());
    }

    @Test
    @DisplayName("품종을 포함한 모든 Animal 조회 테스트")
    void findAnimalAllWithAnimalKindsTest() {
        // given
        Animal animal = Animal.builder()
            .code("1234")
            .name("testAnimal")
            .build();
        entityManager.persist(animal);
        IntStream.rangeClosed(1, 5)
            .mapToObj(iter -> AnimalKind.builder()
                .name("name#" + iter)
                .animal(animal)
                .build())
            .forEach(animalKind -> entityManager.persist(animalKind));
        entityManager.flush();
        entityManager.clear();

        // when
        List<Animal> allAnimals = animalRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(allAnimals).hasSize(1);
                softAssertions.assertThat(allAnimals.get(0).getAnimalKinds()).hasSize(5);
            }
        );
    }

}
