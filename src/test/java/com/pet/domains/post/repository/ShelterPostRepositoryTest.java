package com.pet.domains.post.repository;

import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.domain.ShelterPostBookmark;
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
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaAuditingConfig.class))
@DisplayName("ShelterPost Repository 테스트")
class ShelterPostRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShelterPostRepository shelterPostRepository;

    private ShelterPost shelterPost;

    private Account account;

    @BeforeEach
    void setUp() {
        account = Account.builder()
            .email("test@gmail.com")
            .build();
        entityManager.persist(account);

        Animal animal = Animal.builder()
            .code("111111")
            .name("animal")
            .build();
        entityManager.persist(animal);
        AnimalKind animalKind = AnimalKind.builder()
            .name("animalKind")
            .animal(animal)
            .build();
        entityManager.persist(animalKind);

        City city = City.builder()
            .name("city")
            .code("111111")
            .build();
        entityManager.persist(city);
        Town town = Town.builder()
            .city(city)
            .name("town")
            .build();
        entityManager.persist(town);

        shelterPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .build();
        entityManager.persist(shelterPost);
        entityManager.persist(
            ShelterPost.builder()
                .animalKind(animalKind)
                .town(town)
                .feature("other post")
                .build()
        );
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("북마크 여부를 포함한 조회 테스트")
    void findAllWithIsBookmarkTest() {
        // given
        ShelterPostBookmark postBookmark = ShelterPostBookmark.builder()
            .shelterPost(shelterPost)
            .account(account)
            .build();
        entityManager.persist(postBookmark);
        entityManager.flush();
        entityManager.clear();

        // when
        Page<ShelterPostWithIsBookmark> pageResult =
            shelterPostRepository.findAllWithIsBookmarkAccount(account, PageRequest.of(0, 10));

        // then
        List<ShelterPostWithIsBookmark> contents = pageResult.getContent();
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(contents).hasSize(2);
                softAssertions.assertThat(contents.get(0).getShelterPost().getId()).isEqualTo(shelterPost.getId());
                softAssertions.assertThat(contents.get(0).getIsBookmark()).isTrue();
                softAssertions.assertThat(contents.get(1).getIsBookmark()).isFalse();
                softAssertions.assertThat(pageResult.getTotalElements()).isEqualTo(2L);
            }
        );
    }


}
