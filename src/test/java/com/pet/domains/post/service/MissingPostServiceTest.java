package com.pet.domains.post.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.pet.domains.account.domain.Account;
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
import com.pet.domains.image.service.ImageService;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.mapper.MissingPostMapper;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.post.repository.MissingPostWithIsBookmark;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.service.PostTagService;
import com.pet.domains.tag.service.TagService;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

    @Mock
    private ImageService imageService;

    @Mock
    private MissingPostMapper missingPostMapper;

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
        image = new Image("awern23kjnr2k3n423.jpg");

        group = new Group("1그룹");

        account = Account.builder()
            .email("aaa@naver.com").password("user123").nickname("nickname")
            .notification(false).profileImage(image).group(group).build();

        city = City.builder()
            .code("001")
            .name("서울시")
            .build();

        town = Town.builder()
            .city(city)
            .code("001")
            .name("노원구")
            .build();

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

        postImage = PostImage.builder()
            .missingPost(missingPost)
            .image(image)
            .build();
    }

    @Test
    @DisplayName("실종/보호 게시물 생성 테스트")
    void createMissingPostTest() {
        //given
//        MissingPostCreateParam param = MissingPostCreateParam.of(
//            String.valueOf(missingPost.getStatus()), missingPost.getDate(), city.getId(), town.getId(),
//            missingPost.getDetailAddress(),
//            missingPost.getTelNumber(), animal.getId(), animalKind.getName(), missingPost.getAge(),
//            String.valueOf(missingPost.getSexType()), missingPost.getChipNumber(), missingPost.getContent(),
//            List.of(
//                MissingPostCreateParam.Tag.of(tag.getName())
//            )
//        );
//        MissingPost spyMissingPost = spy(missingPost);
//        given(spyMissingPost.getId()).willReturn(1L);
//        given(animalKindService.getOrCreateAnimalKind(any(), eq("푸들"))).willReturn(animalKind);
//        given(townRepository.getById(any())).willReturn(town);
//        given(tagService.getOrCreateByTagName(any())).willReturn(tag);
//        doNothing().when(postTagService).createPostTag(any(), any());
//        given(imageService.createImage(any())).willReturn(image);
//        given(missingPostMapper.toEntity(any(), any(), any(), any(), any())).willReturn(missingPost);
//
//        //when
//        Long getMissingPostId =
//            missingPostService.createMissingPost(param, List.of(mock(MultipartFile.class)), account);
//
//        //then
//        assertThat(getMissingPostId).isEqualTo(1L);
//
//        verify(animalKindService).getOrCreateAnimalKind(any(), any());
//        verify(townRepository).getById(any());
//        verify(tagService).getOrCreateByTagName(any());
//        verify(postTagService).createPostTag(any(), any());
//        verify(missingPostRepository).save(any());
//        verify(imageService).createImage(any());
//        verify(missingPostMapper).toEntity(any(), any(), any(), any(), any());
    }

    @Test
    @DisplayName("실종/보호 게시물 삭제 테스트")
    void deleteMissingPostTest() {
        //given
        doNothing().when(missingPostRepository).deleteById(any());

        //when
        missingPostRepository.deleteById(1L);

        //then
        verify(missingPostRepository).deleteById(anyLong());
    }

    @Test
    @DisplayName("실종/보호 게시물 익명 조회 테스트")
    void getAnonymousMissingPostTest() {
        //given
        given(missingPostRepository.findAlWithFetch(any())).willReturn(mock(Page.class));

        //when
        Page<MissingPost> pageResult = missingPostRepository.findAlWithFetch(PageRequest.of(1, 5));

        //then
        verify(missingPostRepository, times(1)).findAlWithFetch(any());
    }

    @Test
    @DisplayName("실종/보호 게시물 사용자 조회 테스트")
    void getUserMissingPostTest() {
        //given
        given(missingPostRepository.findAllWithIsBookmarkAccountByDeletedIsFalse(any(), any())).willReturn(
            mock(Page.class));

        //when
        Page<MissingPostWithIsBookmark> pageResult =
            missingPostRepository.findAllWithIsBookmarkAccountByDeletedIsFalse(account, PageRequest.of(1, 5));

        //then
        verify(missingPostRepository, times(1)).findAllWithIsBookmarkAccountByDeletedIsFalse(any(), any());
    }
}
