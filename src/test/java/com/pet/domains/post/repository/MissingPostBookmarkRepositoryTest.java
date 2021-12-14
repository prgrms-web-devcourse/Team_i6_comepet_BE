package com.pet.domains.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.repository.AnimalKindRepository;
import com.pet.domains.animal.repository.AnimalRepository;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.CityRepository;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.MissingPostBookmark;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaAuditingConfig.class))
@DisplayName("MissingPostBookmark RepositoryTest 테스트")
class MissingPostBookmarkRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    TownRepository townRepository;

    @Autowired
    MissingPostRepository missingPostRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    AnimalKindRepository animalKindRepository;

    @Autowired
    MissingPostBookmarkRepository missingPostBookmarkRepository;

    private Account account;

    private City city;

    private Town town;

    private MissingPost missingPost;

    private Animal animal;

    private AnimalKind animalKind;

    private MissingPostBookmark missingPostBookmark;

    @BeforeEach
    void setUp() {
        account = Account.builder()
            .nickname("nickname")
            .email("abvcd@naver.com")
            .build();
        accountRepository.save(account);

        city = City.builder()
            .code("001")
            .name("서울시")
            .build();
        cityRepository.save(city);

        town = Town.builder()
            .city(city)
            .code("001")
            .name("노원구")
            .build();
        townRepository.save(town);

        animal = Animal.builder()
            .code("001")
            .name("강아지")
            .build();
        animalRepository.save(animal);

        animalKind = AnimalKind.builder()
            .code("001")
            .name("푸들")
            .animal(animal)
            .build();
        animalKindRepository.save(animalKind);

        missingPost = MissingPost.builder()
            .status(Status.DETECTION)
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
            .build();
        missingPostRepository.save(missingPost);
    }

    @AfterEach
    void tearDown() {
        missingPostBookmarkRepository.deleteAll();
        missingPostRepository.deleteAll();
        animalKindRepository.deleteAll();
        animalRepository.deleteAll();
        townRepository.deleteAll();
        cityRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("실종/보호 게시물 북마크 삭제 테스트")
    void deleteMissingPost() {
        //given
        missingPostBookmark = MissingPostBookmark.builder()
            .missingPost(missingPost)
            .account(account)
            .build();
        missingPostBookmarkRepository.save(missingPostBookmark);

        //when
        missingPostBookmarkRepository.deleteByMissingPostIdAndAccount(missingPost.getId(), account);

        //then
        List<MissingPostBookmark> getMissingPostBookmarks = missingPostBookmarkRepository.findAll();
        assertThat(getMissingPostBookmarks.size()).isEqualTo(0);
    }

}
