package com.pet.domains.account.repository;

import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Provider;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.domain.Town;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import static org.assertj.core.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = {JpaAuditingConfig.class, QuerydslConfig.class})
)
@DisplayName("회원 Repository 테스트")
class AccountRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AccountRepository accountRepository;

    private Group group;

    @BeforeEach
    void setUp() {
        Permission permission = entityManager.persist(new Permission("ROLE_USER"));
        Group group = entityManager.persist(new Group("USER_GROUP"));
        this.group = entityManager.persist(new GroupPermission(group, permission)).getGroup();
    }

    @Test
    @DisplayName("이메일로 회원 조회 테스트")
    void findByEmailTest() {
        String email = "tester@email.com";
        Account account = givenAccount(email, "tester", group);
        entityManager.persist(account);

        entityManager.flush();
        entityManager.clear();

        Account savedAccount = accountRepository.findByEmail(email).get();
        assertThat(savedAccount).usingRecursiveComparison().ignoringFields("id").isEqualTo(savedAccount);
    }

    @Test
    @DisplayName("town id에 해당하는 시군구를 관심 지역으로 등록한 회원 목록 조회 테스트")
    void findAllByNotificationSubscribersTest() {
        // given
        entityManager.persist(group);
        City city = City.builder()
            .name("경기도")
            .code("111")
            .build();
        entityManager.persist(city);
        Town targetTown = Town.builder()
            .city(city)
            .name("구리시")
            .build();
        entityManager.persist(targetTown);
        Town otherTown = Town.builder()
            .city(city)
            .name("수원시")
            .build();
        entityManager.persist(otherTown);

        Account targetAccount = givenAccount("target@gmail.com", "Subscribers", group);
        entityManager.persist(targetAccount);
        entityManager.persist(InterestArea.builder()
            .account(targetAccount)
            .town(targetTown)
            .selected(true)
            .build()
        );

        Account nonTargetAccount1 = givenAccount("nonTarget1@gmail.com", "nope", group);
        nonTargetAccount1.updateNotification(false);
        entityManager.persist(nonTargetAccount1);
        entityManager.persist(InterestArea.builder()
            .account(nonTargetAccount1)
            .town(targetTown)
            .selected(true)
            .build()
        );
        Account nonTargetAccount2 = givenAccount("nonTarget2@gmail.com", "nope", group);
        entityManager.persist(nonTargetAccount2);
        entityManager.persist(InterestArea.builder()
            .account(nonTargetAccount2)
            .town(otherTown)
            .selected(true)
            .build()
        );
        entityManager.flush();
        entityManager.clear();

        // when
        List<Account> candidates =
            accountRepository.findAllByNotificationSubscribers(targetTown.getId(), nonTargetAccount2.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(candidates).hasSize(1);
                softAssertions.assertThat(candidates.get(0).getId()).isEqualTo(targetAccount.getId());
                softAssertions.assertThat(candidates.get(0).isNotification()).isTrue();
            }
        );
    }

    private Account givenAccount(String email, String nickname, Group group) {
        return Account.builder()
            .email(email)
            .password("$2a$10$21Pd/Fr9GAN9Js6FmvahmuBMEZo73FSBUpDPXl2lTIyLWSqnQoaqi")
            .nickname(nickname)
            .notification(true)
            .checkedArea(true)
            .profileImage(null)
            .group(group)
            .provider(Provider.LOCAL)
            .build();
    }

}
