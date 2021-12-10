package com.pet.domains.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.SignStatus;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.service.AnimalKindService;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.repository.TownRepository;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.image.repository.PostImageRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.service.PostTagService;
import com.pet.domains.tag.service.TagService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("실종/보호 게시물 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MissingPostServiceTest {

    @InjectMocks
    private MissingPostService missingPostService;

    @Mock
    private MissingPostRepository missingPostRepository;

    @Mock
    private AnimalKindService animalKindService;

    @Mock
    private TownRepository townRepository;

    @Mock
    private PostImageRepository postImageRepository;

    @Mock
    private PostTagService postTagService;

    @Mock
    private TagService tagService;

    private Group group;

    private Image image;

    private Account account;

    private City city;

    private Town town;

    private Tag tag;

    private Animal animal;

    private AnimalKind animalKind;

    private PostImage postImage;

    private MissingPost missingPost;

    @BeforeEach
    void setUp() {
        image = Image.builder()
            .name("awern23kjnr2k3n423.jpg")
            .build();

        group = new Group("1그룹");

        account =
            new Account("aaa@naver.com", "user123", "nickName", false, false, SignStatus.VERIFICATION, image,
                group);

        city = City.builder()
            .code("001")
            .name("서울시")
            .build();

        town = Town.builder()
            .city(city)
            .code("001")
            .name("노원구")
            .build();
        townRepository.save(town);

        tag = Tag.builder()
            .name("웰시코기")
            .count(0)
            .build();

        animal = Animal.builder()
            .code("001")
            .name("강아지")
            .build();

        animalKind = AnimalKind.builder()
            .code("001")
            .name("푸들")
            .animal(animal)
            .build();

        missingPost = MissingPost.builder()
            .id(1L)
            .status(Status.DETECTION)
            .detailAddress("상세주소")
            .date(LocalDate.now())
            .age(10)
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

        postImage = PostImage.builder()
            .missingPost(missingPost)
            .image(image)
            .build();
    }

    @Test
    @DisplayName("실종/보호 게시물 생성 테스트")
    void createMissingPostTest() {
        //given
        List<Image> images = List.of(image);
        MissingPostCreateParam param = MissingPostCreateParam.of(
            String.valueOf(missingPost.getStatus()), missingPost.getDate(), city.getId(), town.getId(),
            missingPost.getDetailAddress(),
            missingPost.getTelNumber(), animal.getId(), animalKind.getName(), missingPost.getAge(),
            String.valueOf(missingPost.getSexType()), missingPost.getChipNumber(), missingPost.getContent()
            , List.of(
                MissingPostCreateParam.Tag.of(tag.getName())
            )
        );
        given(animalKindService.getOrCreateByAnimalKind(any(), any())).willReturn(animalKind);
        given(townRepository.findById(any())).willReturn(Optional.ofNullable(town));
        given(tagService.getOrCreateByTagName(any())).willReturn(tag);
        doNothing().when(postTagService).createPostTag(any(), any());
        given(postImageRepository.save(any())).willReturn(postImage);
        given(missingPostRepository.save(any())).willReturn(missingPost);

        //when
        Long getMissingPostId = missingPostService.createMissingPost(param, images);

        //then
        assertThat(getMissingPostId).isEqualTo(1L);

        verify(animalKindService).getOrCreateByAnimalKind(any(), any());
        verify(townRepository).findById(any());
        verify(tagService).getOrCreateByTagName(any());
        verify(postTagService).createPostTag(any(), any());
        verify(postImageRepository).save(any());
        verify(missingPostRepository).save(any());
    }

}
