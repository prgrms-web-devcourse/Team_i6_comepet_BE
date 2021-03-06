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
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.post.dto.serach.PostSearchParam;
import com.pet.domains.post.repository.MissingPostRepository;
import com.pet.domains.post.repository.projection.MissingPostWithIsBookmark;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
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


    private Group group;

    private Image image;

    private Account account;

    private City city;

    private Town town;

    private Tag tag;

    private Animal animal;

    private AnimalKind animalKind;

    private MissingPost missingPost;

    private PostImage postImage;

    private PostTag postTag;


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

        postTag = PostTag.builder()
            .missingPost(missingPost)
            .tag(tag)
            .build();

        postImage = PostImage.builder()
            .missingPost(missingPost)
            .image(image)
            .build();
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
    @DisplayName("실종/보호 게시물 리스트 익명 조회 테스트")
    void getAnonymousMissingPostsTest() {
        //given
        given(missingPostRepository.findMissingPostAllWithFetch(any(), any())).willReturn(mock(Page.class));

        //when
        Page<MissingPost> pageResult = missingPostRepository.findMissingPostAllWithFetch(any(), any());

        //then
        verify(missingPostRepository, times(1)).findMissingPostAllWithFetch(any(), any());
    }

    @Test
    @DisplayName("실종/보호 게시물 리스트 사용자 조회 테스트")
    void getUserMissingPostsTest() {
        //given
        given(missingPostRepository.findMissingPostAllWithIsBookmark(any(), any(), any())).willReturn(
            mock(Page.class));

        //when
        Page<MissingPostWithIsBookmark> pageResult =
            missingPostRepository
                .findMissingPostAllWithIsBookmark(account, PageRequest.of(1, 5), PostSearchParam.builder().build());

        //then
        verify(missingPostRepository, times(1)).findMissingPostAllWithIsBookmark(any(), any(), any());
    }

}
