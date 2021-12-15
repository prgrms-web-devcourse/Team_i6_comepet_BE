package com.pet.domains.account.repository;

import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
import com.pet.domains.account.domain.Provider;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.CityRepository;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.auth.repository.GroupPermissionRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.MissingPostBookmark;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.repository.MissingPostBookmarkRepository;
import com.pet.domains.post.repository.MissingPostRepository;
import java.time.LocalDate;
import javax.persistence.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaAuditingConfig.class))
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

    @Test
    @DisplayName("알림 삭제 테스트")
    void deleteNotification() {
        Notification notification =
            Notification.builder().account(account).checked(true).missingPost(missingPost).build();

        Notification save = notificationRepository.save(notification);

        assertThat(notificationRepository.findAll().size()).isEqualTo(1);

        notificationRepository.deleteByIdAndAccount(save.getId(), account);

        assertThat(notificationRepository.findAll().size()).isEqualTo(0);
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
