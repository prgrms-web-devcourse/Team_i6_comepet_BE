package com.pet.domains.area.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaAuditingConfig.class))
@DisplayName("TownRepository 테스트")
class TownRepositoryTest {

    @Autowired
    private CityRepository cityRepository;

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
        cityRepository.save(city);

        Town town = new Town("001", "노원구", city);
        townRepository.save(town);

        //when
        Town foundTown = townRepository.getById(1L);

        //then
        assertThat(foundTown.getId()).isEqualTo(town.getId());
    }

}