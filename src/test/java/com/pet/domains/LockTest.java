package com.pet.domains;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.pet.common.util.OptimisticLockingHandlingUtils;
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
import com.pet.domains.auth.domain.Group;
import com.pet.domains.auth.domain.GroupPermission;
import com.pet.domains.auth.domain.Permission;
import com.pet.domains.auth.repository.GroupPermissionRepository;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.repository.ShelterPostBookmarkRepository;
import com.pet.domains.post.repository.ShelterPostRepository;
import com.pet.domains.post.service.ShelterPostBookmarkService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class LockTest {

    @Autowired
    private GroupPermissionRepository groupPermissionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalKindRepository animalKindRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private TownRepository townRepository;

    @Autowired
    private ShelterPostRepository shelterPostRepository;

    @Autowired
    private ShelterPostBookmarkRepository shelterPostBookmarkRepository;

    @Autowired
    private ShelterPostBookmarkService shelterPostBookmarkService;

    private Account account;

    private AnimalKind animalKind;

    private Town town;

    @BeforeEach
    void setUp() {
        GroupPermission groupPermission = groupPermissionRepository.save(
            new GroupPermission(new Group("USER_GROUP"), new Permission("ROLE_USER"))
        );

        account = Account.builder()
            .email("test@gmail.com")
            .nickname("nickname")
            .password("123123a!")
            .group(groupPermission.getGroup())
            .build();
        accountRepository.save(account);

        Animal animal = Animal.builder()
            .code("111111")
            .name("animal")
            .build();
        animalRepository.save(animal);
        animalKind = AnimalKind.builder()
            .name("animalKind")
            .animal(animal)
            .build();
        animalKindRepository.save(animalKind);

        City city = City.builder()
            .name("city")
            .code("111111")
            .build();
        cityRepository.save(city);
        town = Town.builder()
            .city(city)
            .name("town")
            .build();
        townRepository.save(town);
    }

    @AfterEach
    void afterEach() {
        shelterPostBookmarkRepository.deleteAllInBatch();
        shelterPostRepository.deleteAllInBatch();
        townRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();
        animalKindRepository.deleteAllInBatch();
        animalRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
        groupPermissionRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("북마크 카운트 증감 테스트 - 낙관적 락 X")
    void createPostBookmarkWithoutOptimisticLockingHandlingTest() throws InterruptedException {
        // given
        ShelterPost shelterPost = shelterPostRepository.save(ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .build()
        );

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                shelterPostBookmarkService.createPostBookmark(shelterPost.getId(), account);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        ShelterPost foundShelterPost = shelterPostRepository.findById(shelterPost.getId()).get();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundShelterPost.getBookmarkCount()).isLessThan(10);
                softAssertions.assertThatCode(
                    () -> verify(shelterPostBookmarkService, times(10))
                        .createPostBookmark(anyLong(), any(Account.class)));
            }
        );
    }

    @Test
    @DisplayName("북마크 카운트 증감 테스트 - 낙관적 락 O")
    void createPostBookmarkWithOptimisticLockingHandlingTest() throws InterruptedException {
        // given
        ShelterPost shelterPost = shelterPostRepository.save(ShelterPost.builder()
            .animalKind(animalKind)
            .town(town)
            .build()
        );

        // when
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                OptimisticLockingHandlingUtils.handling(
                    () -> shelterPostBookmarkService.createPostBookmark(shelterPost.getId(), account),
                    10,
                    "실종 게시글 북마크 생성"
                );
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
        ShelterPost foundShelterPost = shelterPostRepository.findById(shelterPost.getId()).get();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundShelterPost.getBookmarkCount()).isEqualTo(10);
                softAssertions.assertThatCode(
                    () -> verify(shelterPostBookmarkService, times(10))
                        .createPostBookmark(anyLong(), any(Account.class)));
            }
        );
    }

}
