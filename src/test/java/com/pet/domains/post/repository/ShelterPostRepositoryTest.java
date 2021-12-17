package com.pet.domains.post.repository;

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
import com.pet.domains.auth.repository.GroupPermissionRepository;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.domain.ShelterPostBookmark;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.repository.projection.ShelterPostWithIsBookmark;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = {JpaAuditingConfig.class, QuerydslConfig.class})
)
@DisplayName("보호소 게시글 리포지토리 테스트")
class ShelterPostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShelterPostRepository shelterPostRepository;

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    private Account account;

    private Animal animal;

    private AnimalKind animalKind;

    private City city;

    private Town town;

    private GroupPermission groupPermission;

    @BeforeEach
    void setUp() {
        groupPermission = groupPermissionRepository.save(
            new GroupPermission(new Group("USER_GROUP"), new Permission("ROLE_USER"))
        );

        account = Account.builder()
            .email("test@gmail.com")
            .nickname("nickname")
            .password("123123a!")
            .group(groupPermission.getGroup())
            .build();
        entityManager.persist(account);

        animal = Animal.builder()
            .code("111111")
            .name("animal")
            .build();
        entityManager.persist(animal);
        animalKind = AnimalKind.builder()
            .name("animalKind")
            .animal(animal)
            .build();
        entityManager.persist(animalKind);

        city = City.builder()
            .name("city")
            .code("111111")
            .build();
        entityManager.persist(city);
        town = Town.builder()
            .city(city)
            .name("town")
            .build();
        entityManager.persist(town);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("보호소 게시글 페이지 조회 테스트 - 북마크 여부 O")
    void findAllWithIsBookmarkTest() {
        // given
        ShelterPost nonBookmarkPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .sex(SexType.FEMALE)
            .build();
        entityManager.persist(nonBookmarkPost);
        ShelterPost nonBookmarkAndUnknownPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .sex(SexType.UNKNOWN)
            .build();
        entityManager.persist(nonBookmarkAndUnknownPost);
        ShelterPost bookmarkPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .sex(SexType.UNKNOWN)
            .feature("bookmark post")
            .build();
        entityManager.persist(bookmarkPost);
        ShelterPostBookmark postBookmark = ShelterPostBookmark.builder()
            .shelterPost(bookmarkPost)
            .account(account)
            .build();
        entityManager.persist(postBookmark);
        entityManager.flush();
        entityManager.clear();

        // when
        Page<ShelterPostWithIsBookmark> pageResult = shelterPostRepository.findAllWithIsBookmark(
            account,
            PageRequest.of(0, 10),
            PostSearchParam.builder()
                .city(city.getId())
                .sex(SexType.UNKNOWN)
                .build()
        );

        // then
        List<ShelterPostWithIsBookmark> contents = pageResult.getContent();
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(contents).hasSize(2);
                softAssertions.assertThat(contents.get(0).getShelterPost().getId())
                    .isEqualTo(nonBookmarkAndUnknownPost.getId());
                softAssertions.assertThat(contents.get(0).isBookmark()).isFalse();
                softAssertions.assertThat(contents.get(1).getShelterPost().getId()).isEqualTo(bookmarkPost.getId());
                softAssertions.assertThat(contents.get(1).isBookmark()).isTrue();
                softAssertions.assertThat(pageResult.getTotalElements()).isEqualTo(2L);
            }
        );
    }

    @Test
    @DisplayName("보호소 게시글 페이지 조회 테스트 - 북마크 여부 X")
    void findAllWithFetchTest() {
        // given
        ShelterPost nonBookmarkPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .build();
        entityManager.persist(nonBookmarkPost);
        ShelterPost bookmarkPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .feature("bookmark post")
            .build();
        entityManager.persist(bookmarkPost);
        ShelterPostBookmark postBookmark = ShelterPostBookmark.builder()
            .shelterPost(bookmarkPost)
            .account(account)
            .build();
        entityManager.persist(postBookmark);
        entityManager.flush();
        entityManager.clear();

        // when
        Page<ShelterPost> pageResult =
            shelterPostRepository.findAllWithFetch(PageRequest.of(0, 10), PostSearchParam.builder().build());

        // then
        List<ShelterPost> contents = pageResult.getContent();
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(contents).hasSize(2);
                softAssertions.assertThat(contents.get(0).getId()).isEqualTo(nonBookmarkPost.getId());
                softAssertions.assertThat(contents.get(1).getId()).isEqualTo(bookmarkPost.getId());
                softAssertions.assertThat(pageResult.getTotalElements()).isEqualTo(2L);
            }
        );
    }

    @Test
    @DisplayName("단건 조회 테스트 - 북마크 여부 O")
    void findByIdWithIsBookmarkTest() {
        // given
        ShelterPost bookMarkPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .feature("bookmark post")
            .build();
        entityManager.persist(bookMarkPost);
        ShelterPostBookmark postBookmark = ShelterPostBookmark.builder()
            .shelterPost(bookMarkPost)
            .account(account)
            .build();
        entityManager.persist(postBookmark);
        entityManager.flush();
        entityManager.clear();

        // when
        ShelterPostWithIsBookmark result = shelterPostRepository.findByIdWithIsBookmark(account, bookMarkPost.getId())
            .get();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(result.getShelterPost().getId()).isEqualTo(bookMarkPost.getId());
                softAssertions.assertThat(result.isBookmark()).isTrue();
            }
        );
    }

    @Test
    @DisplayName("단건 조회 테스트 - 북마크 여부 X")
    void findByIdWithFetchTest() {
        // given
        ShelterPost bookMarkPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .feature("bookmark post")
            .build();
        entityManager.persist(bookMarkPost);
        ShelterPostBookmark postBookmark = ShelterPostBookmark.builder()
            .shelterPost(bookMarkPost)
            .account(account)
            .build();
        entityManager.persist(postBookmark);
        entityManager.flush();
        entityManager.clear();

        // when
        ShelterPost foundPost = shelterPostRepository.findByIdWithFetch(bookMarkPost.getId()).get();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundPost.getId()).isEqualTo(bookMarkPost.getId());
            }
        );
    }
}
