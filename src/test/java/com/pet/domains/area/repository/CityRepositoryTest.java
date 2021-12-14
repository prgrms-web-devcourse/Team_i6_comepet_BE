package com.pet.domains.area.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.area.domain.City;
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
@DisplayName("CityRepository 테스트")
class CityRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CityRepository cityRepository;

    @Test
    @DisplayName("코드로 city 조회 테스트")
    void findCityByCodeTest() {
        // given
        City city = City.builder()
            .code("1234")
            .name("city")
            .build();
        entityManager.persist(city);

        // when
        City foundCity = cityRepository.findByCode("1234").get();

        // then
        assertThat(foundCity.getId()).isEqualTo(city.getId());
    }

    @Test
    @DisplayName("이름로 city 조회 테스트")
    void findCityByNameTest() {
        // given
        City city = City.builder()
            .code("1234")
            .name("city")
            .build();
        entityManager.persist(city);

        // when
        City foundCity = cityRepository.findByName("city").get();

        // then
        assertThat(foundCity.getId()).isEqualTo(city.getId());
    }

}
