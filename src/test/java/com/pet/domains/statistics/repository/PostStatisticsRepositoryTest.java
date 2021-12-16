package com.pet.domains.statistics.repository;

import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.statistics.domain.PostStatistics;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
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

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = {JpaAuditingConfig.class, QuerydslConfig.class}
))@DisplayName("통계 리포지토리 테스트")
class PostStatisticsRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PostStatisticsRepository postStatisticsRepository;

    private Account account;

    private City city;

    private Town town;

    private Animal animal;

    private AnimalKind animalKind;

    @BeforeEach
    void setUp() {
        GroupPermission groupPermission = new GroupPermission(
            new Group("USER_GROUP"), new Permission("ROLE_USER"));
        entityManager.persist(
            groupPermission
        );

        account = Account.builder()
            .nickname("nickname")
            .email("abvcd@naver.com")
            .password("123123a!")
            .group(groupPermission.getGroup())
            .build();
        entityManager.persist(account);

        city = City.builder()
            .code("001")
            .name("서울시")
            .build();
        entityManager.persist(city);

        town = Town.builder()
            .city(city)
            .code("001")
            .name("노원구")
            .build();
        entityManager.persist(town);

        animal = Animal.builder()
            .code("001")
            .name("강아지")
            .build();
        entityManager.persist(animal);

        animalKind = AnimalKind.builder()
            .code("001")
            .name("푸들")
            .animal(animal)
            .build();
        entityManager.persist(animalKind);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("게시글 상태별 개수 조회 테스트")
    void findGroupByStatusTest() {
        // given
        LongStream.rangeClosed(1, 2).forEach(idx -> entityManager.persist(
            MissingPost.builder()
                .status(Status.MISSING)
                .detailAddress("상세주소")
                .date(LocalDate.now())
                .age(10L)
                .sexType(SexType.MALE)
                .chipNumber("010101003")
                .content("content")
                .telNumber("01033342231")
                .viewCount(0)
                .bookmarkCount(0)
                .commentCount(0)
                .thumbnail("asdfasdfsad.jpg")
                .account(account)
                .town(town)
                .animalKind(animalKind)
                .build()
        ));
        LongStream.rangeClosed(1, 2).forEach(idx -> entityManager.persist(
            MissingPost.builder()
                .status(Status.COMPLETION)
                .detailAddress("상세주소")
                .date(LocalDate.now())
                .age(10L)
                .sexType(SexType.MALE)
                .chipNumber("010101003")
                .content("content")
                .telNumber("01033342231")
                .viewCount(0)
                .bookmarkCount(0)
                .commentCount(0)
                .thumbnail("asdfasdfsad.jpg")
                .account(account)
                .town(town)
                .animalKind(animalKind)
                .build()
        ));
        entityManager.flush();
        entityManager.clear();

        // when
        List<PostCountByStatus> results = postStatisticsRepository.findGroupByStatus();
        Map<Status, Long> statusCountMap = results.stream()
            .collect(Collectors.toMap(PostCountByStatus::getPostStatus, PostCountByStatus::getCount));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(statusCountMap.getOrDefault(Status.MISSING, 0L)).isEqualTo(2);
                softAssertions.assertThat(statusCountMap.getOrDefault(Status.PROTECTION, 0L)).isEqualTo(0);
                softAssertions.assertThat(statusCountMap.getOrDefault(Status.DETECTION, 0L)).isEqualTo(0);
                softAssertions.assertThat(statusCountMap.getOrDefault(Status.COMPLETION, 0L)).isEqualTo(2);
            }
        );
    }

    @Test
    @DisplayName("가장 최신의 게시글 통계 데이터 조회 테스트")
    void findFirstByOrderByIdDescTest() {
        // given
        LongStream.rangeClosed(1, 3).forEach(idx -> entityManager.persist(
            PostStatistics.builder()
                .missing(idx)
                .detection(idx)
                .protection(idx)
                .completion(idx)
                .date(LocalDateTime.now())
                .build())
        );
        entityManager.flush();
        entityManager.clear();

        // when
        PostStatistics postStatistics = postStatisticsRepository.findFirstByOrderByIdDesc().get();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(postStatistics.getMissing()).isEqualTo(3);
                softAssertions.assertThat(postStatistics.getDetection()).isEqualTo(3);
                softAssertions.assertThat(postStatistics.getProtection()).isEqualTo(3);
                softAssertions.assertThat(postStatistics.getCompletion()).isEqualTo(3);
            }
        );
    }

}
