package com.pet.domains.animal.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.animal.domain.Animal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = JpaAuditingConfig.class
))
class AnimalRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AnimalRepository animalRepository;

    @Test
    @DisplayName("코드로 동물 조회 테스트")
    void findAnimalKindByNameTest() {
        //Given
        Animal animal = Animal.builder()
            .code("1234")
            .name("testAnimal")
            .build();
        entityManager.persist(animal);
        entityManager.flush();

        //When
        Animal foundAnimal = animalRepository.findByCode("1234").get();

        //Then
        assertThat(foundAnimal.getId()).isNotNull();
    }

}
