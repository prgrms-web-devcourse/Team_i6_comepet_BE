package com.pet.domains.account.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
import com.pet.domains.account.domain.Provider;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.CityRepository;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.auth.repository.GroupPermissionRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.repository.MissingPostRepository;
import java.time.LocalDate;
import java.util.stream.LongStream;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = {JpaAuditingConfig.class, QuerydslConfig.class})
)
@DisplayName("알림 Repository 테스트")
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MissingPostRepository missingPostRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Group group;

    private Account account;

    private MissingPost missingPost;

    @BeforeEach
    void setUp() {
        group = groupPermissionRepository.save(
            new GroupPermission(new Group("USER_GROUP"), new Permission("ROLE_USER"))).getGroup();
        account = givenAccount("tester@email.com", "nickname", group);
        this.account = accountRepository.save(account);
        missingPost = givenPost(accountRepository.save(account));
        missingPostRepository.save(missingPost);
    }

    @AfterEach
    void tearDown() {
        notificationRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("알림 아이디와 회원 아이디로 알림 조회 테스트")
    void checkNotificationTest() {
        Notification save = notificationRepository.save(Notification.builder()
            .account(account).checked(true).missingPost(missingPost).build());

        entityManager.flush();
        entityManager.clear();

        Notification findNotification = notificationRepository.findByIdAndAccount(save.getId(), account).get();

        assertThat(findNotification.getAccount()).isEqualTo(account);
    }


    @Test
    @DisplayName("알림 삭제 테스트")
    void deleteNotificationTest() {
        Notification notification =
            Notification.builder().account(account).checked(true).missingPost(missingPost).build();

        Notification save = notificationRepository.save(notification);

        assertThat(notificationRepository.findAll().size()).isEqualTo(1);

        notificationRepository.deleteByIdAndAccount(save.getId(), account);

        assertThat(notificationRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("알림 페이징 조회")
    void getNotificationPagingTest() {
        LongStream.rangeClosed(0, 10)
            .forEach(index -> notificationRepository
                .save(Notification.builder().account(account).checked(true).missingPost(missingPost).build()));

        Page<Notification> notifications = notificationRepository.findAll(PageRequest.of(0, 8));
        assertThat(notifications.getTotalElements()).isEqualTo(11);
        assertThat(notifications.getContent().size()).isEqualTo(8);
    }

    private Account givenAccount(String email, String nickname, Group group) {
        return new Account(
            9127364171L,
            email,
            "$2a$10$21Pd/Fr9GAN9Js6FmvahmuBMEZo73FSBUpDPXl2lTIyLWSqnQoaqi",
            nickname,
            true,
            true,
            null,
            group,
            Provider.LOCAL);
    }

    private MissingPost givenPost(Account account) {
        City city = cityRepository.save(City.builder().name("서울시").code("111").build());
        Town town = townRepository.save(Town.builder().city(city).name("도봉구").code("31231").build());
        return MissingPost.builder()
            .status(Status.DETECTION)
            .date(LocalDate.now())
            .sexType(SexType.FEMALE)
            .content("내용")
            .telNumber("11111")
            .account(account)
            .viewCount(4)
            .town(town)
            .build();
    }

}
