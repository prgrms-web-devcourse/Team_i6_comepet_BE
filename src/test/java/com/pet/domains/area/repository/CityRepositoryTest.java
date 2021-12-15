package com.pet.domains.area.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import java.util.List;
import java.util.stream.Collectors;
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
@DisplayName("시군구 리포지토리 테스트")
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

    @Test
    @DisplayName("시도/시군구 조회")
    void getAll() {
        // given
        City city1 = entityManager.persist(City.builder().code("11111").name("서울시").build());
        City city2 = entityManager.persist(City.builder().code("11112").name("행복시").build());

        Town town1 = Town.builder().city(city1).code("123").name("도봉구").build();
        Town town2 = Town.builder().city(city1).code("124").name("강동구").build();
        Town town3 = Town.builder().city(city1).code("125").name("강북구").build();

        Town town4 = Town.builder().city(city2).code("125").name("얼씨구").build();
        Town town5 = Town.builder().city(city2).code("125").name("어디구").build();


        entityManager.persist(town1);
        entityManager.persist(town2);
        entityManager.persist(town3);
        entityManager.persist(town4);
        entityManager.persist(town5);

        entityManager.flush();
        entityManager.clear();

        // when
        List<City> cities = cityRepository.findAll();

        // then
        assertThat(cities.size()).isEqualTo(2);

        City city = cities.get(0);
        assertThat(city.getName()).isEqualTo("서울시");

        List<String> townNames = city.getTowns().stream().map(Town::getName).collect(Collectors.toList());
        assertThat(townNames).containsExactly("도봉구", "강동구", "강북구");
    }


}
