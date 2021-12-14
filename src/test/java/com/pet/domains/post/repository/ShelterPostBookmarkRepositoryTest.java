package com.pet.domains.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.domain.ShelterPostBookmark;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JpaAuditingConfig.class))
@DisplayName("ShelterPostBookmark Repository  테스트")
class ShelterPostBookmarkRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ShelterPostBookmarkRepository shelterPostBookmarkRepository;

    @Test
    @DisplayName("보호소 게시글 북마크 삭제 테스트")
    void deleteByShelterPostIdAndAccountTest() {
        // given
        Account account = Account.builder()
            .email("test@gmail.com")
            .nickname("nickname")
            .password("123123a!")
            .group(entityManager.persist(new Group("USER_GROUP")))
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

        ShelterPost shelterPost = ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .build();
        entityManager.persist(shelterPost);
        ShelterPostBookmark shelterPostBookmark = ShelterPostBookmark.builder()
            .shelterPost(shelterPost)
            .account(account)
            .build();
        entityManager.persist(shelterPostBookmark);
        entityManager.flush();
        entityManager.clear();

        // when
        shelterPostBookmarkRepository.deleteByShelterPostIdAndAccount(shelterPost.getId(), account);
        List<ShelterPostBookmark> bookmarks = shelterPostBookmarkRepository.findAll();

        // then
        assertThat(bookmarks).isEmpty();
    }
}
