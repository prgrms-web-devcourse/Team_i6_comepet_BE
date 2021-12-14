package com.pet.domains.area.repository;

import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.SignStatus;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.domain.Town;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaAuditingConfig.class))
@DisplayName("관심 지역 repository 테스트")
class InterestAreaRepositoryTest {

    @Autowired
    private InterestAreaRepository interestAreaRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TestEntityManager entityManager;

    @DisplayName("회원 관심 지역 조회 테스트")
    @Test
    void saveInterestArea() {
        Account account = accountRepository.save(givenAccount());
        City city = cityRepository.save(City.builder().code("100").name("서울시").build());
        Town town1 = townRepository.save(Town.builder().name("도봉구").code("111").city(city).build());
        Town town2 = townRepository.save(Town.builder().name("강북구").code("112").city(city).build());

        interestAreaRepository.save(InterestArea.builder().town(town1).selected(true).account(account).build());
        interestAreaRepository.save(InterestArea.builder().town(town2).selected(true).account(account).build());

        entityManager.flush();
        entityManager.clear();

        List<InterestArea> interestAreas = interestAreaRepository.findByAccountId(account.getId());

        assertThat(interestAreas.size()).isEqualTo(2);

        // fetch join
        assertThat(interestAreas.get(0).getTown().getName()).isEqualTo("도봉구");
        assertThat(interestAreas.get(0).getTown().getCity().getName()).isEqualTo("서울시");
    }

    private Account givenAccount() {
        return Account.builder().email("tseter@email.com").password("1234").nickname("name").notification(true)
            .checkedArea(true).signStatus(SignStatus.SUCCESS).build();
    }

}