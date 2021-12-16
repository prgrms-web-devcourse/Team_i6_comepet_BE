package com.pet.domains.area.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
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
@DisplayName("시군구 리포지토리 테스트")
class TownRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TownRepository townRepository;

    @Test
    @DisplayName("id로 town 조회 테스트")
    void findTownByIdTest() {
        //given
        City city = City.builder()
            .name("서울시")
            .code("001")
            .build();
        entityManager.persist(city);

        Town town = new Town("001", "노원구", city);
        entityManager.persist(town);
        entityManager.flush();
        entityManager.clear();

        //when
        Town foundTown = townRepository.getById(town.getId());

        //then
        assertThat(foundTown.getId()).isEqualTo(town.getId());
    }

    @Test
    @DisplayName("시군구 이름 일치 & 시도 조건으로 조회 테스트")
    void findByNameAndCityTest() {
        //given
        City city = City.builder()
            .name("서울시")
            .code("001")
            .build();
        entityManager.persist(city);

        Town town = new Town("001", "노원구", city);
        entityManager.persist(town);
        entityManager.flush();
        entityManager.clear();

        //when
        Town foundTown = townRepository.findByNameAndCity("노원구", city).get();

        //then
        assertThat(foundTown.getId()).isEqualTo(town.getId());
    }

    @Test
    @DisplayName("시군구 이름 containing & 시도 조건으로 조회 테스트")
    void findByNameContainingAndCityTest() {
        //given
        City city = City.builder()
            .name("서울시")
            .code("001")
            .build();
        entityManager.persist(city);

        Town town = new Town("001", "노원구 어쩌구저쩌구", city);
        entityManager.persist(town);
        entityManager.flush();
        entityManager.clear();

        //when
        Town foundTown = townRepository.findByNameContainingAndCity("노원구", city).get();

        //then
        assertThat(foundTown.getId()).isEqualTo(town.getId());
    }

}
