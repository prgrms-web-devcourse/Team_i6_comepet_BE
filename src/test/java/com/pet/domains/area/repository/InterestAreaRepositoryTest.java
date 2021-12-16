package com.pet.domains.area.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.domain.Town;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.auth.repository.GroupPermissionRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
    classes = {JpaAuditingConfig.class, QuerydslConfig.class}
))@DisplayName("관심 지역 리포지토리 테스트")
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

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    private Account account;

    private City city;

    private Town town1;

    private Town town2;

    private GroupPermission groupPermission;

    @BeforeEach
    void setUp() {
        groupPermission = groupPermissionRepository.save(
            new GroupPermission(new Group("USER_GROUP"), new Permission("ROLE_USER"))
        );
        account = accountRepository.save(givenAccount());
        city = cityRepository.save(City.builder().code("100").name("서울시").build());
        town1 = townRepository.save(Town.builder().name("도봉구").code("111").city(city).build());
        town2 = townRepository.save(Town.builder().name("강북구").code("112").city(city).build());
    }

    @AfterEach
    void tearDown() {
        interestAreaRepository.deleteAll();
    }

    @DisplayName("회원 관심 지역 조회 테스트")
    @Test
    void getInterestAreaTest() {
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

    @DisplayName("회원 관심 지역 삭제 테스트")
    @Test
    void saveInterestAreaTest() {
        interestAreaRepository.save(InterestArea.builder().town(town1).selected(true).account(account).build());
        interestAreaRepository.save(InterestArea.builder().town(town2).selected(true).account(account).build());

        entityManager.flush();
        entityManager.clear();

        interestAreaRepository.deleteAllByAccountId(account.getId());
        assertThat(interestAreaRepository.findAll().size()).isEqualTo(0);
    }

    private Account givenAccount() {
        return Account.builder().email("tester@email.com").password("1234")
            .group(groupPermission.getGroup()).nickname("name").notification(true)
            .checkedArea(true).build();
    }

}
