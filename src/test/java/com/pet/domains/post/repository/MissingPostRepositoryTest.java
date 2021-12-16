package com.pet.domains.post.repository;

import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.exception.ExceptionMessage;
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
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.comment.repository.CommentRepository;
import com.pet.domains.image.domain.Image;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.image.repository.ImageRepository;
import com.pet.domains.image.repository.PostImageRepository;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import com.pet.domains.tag.repository.PostTagRepository;
import com.pet.domains.tag.repository.TagRepository;
import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
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
@DisplayName("실종 게시글 리포지토리 테스트")
class MissingPostRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    TownRepository townRepository;

    @Autowired
    MissingPostRepository missingPostRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    PostImageRepository postImageRepository;

    @Autowired
    TagRepository tagRepository;

    @Autowired
    PostTagRepository postTagRepository;

    @Autowired
    AnimalRepository animalRepository;

    @Autowired
    AnimalKindRepository animalKindRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    GroupPermissionRepository groupPermissionRepository;

    private Account account;

    private City city;

    private Town town;

    private MissingPost missingPost;

    private Image image;

    private PostImage postImage;

    private Tag tag;

    private PostTag postTag;

    private Animal animal;

    private AnimalKind animalKind;

    private Comment comment;

    private GroupPermission groupPermission;

    @BeforeEach
    void setUp() {
        groupPermission = groupPermissionRepository.save(
            new GroupPermission(new Group("USER_GROUP"), new Permission("ROLE_USER"))
        );

        account = Account.builder()
            .nickname("nickname")
            .email("abvcd@naver.com")
            .password("123123a!")
            .group(groupPermission.getGroup())
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

        tag = Tag.builder()
            .name("웰시코기")
            .count(1)
            .build();
        tagRepository.save(tag);

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

        image = new Image("awern23kjnr2k3n423.jpg");
        imageRepository.save(image);

        postTag = PostTag
            .builder()
            .missingPost(missingPost)
            .tag(tag)
            .build();
        missingPostRepository.save(missingPost);

        postImage = PostImage.builder()
            .missingPost(missingPost)
            .image(image)
            .build();
        postImageRepository.save(postImage);

        comment = Comment.builder()
            .missingPost(missingPost)
            .content("내용")
            .account(account)
            .build();
        commentRepository.save(comment);
    }

    @AfterEach
    void tearDown() {
        commentRepository.deleteAllInBatch();
        postImageRepository.deleteAllInBatch();
        imageRepository.deleteAllInBatch();
        missingPostRepository.deleteAllInBatch();
        tagRepository.deleteAllInBatch();
        townRepository.deleteAllInBatch();
        cityRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("실종/보호 게시물 삭제 테스트")
    void deleteMissingPostTest() {
        //given
        //when
        MissingPost getMissingPost = missingPostRepository.findById(missingPost.getId())
            .filter(post -> post.getAccount().getId().equals(account.getId()))
            .orElseThrow(ExceptionMessage.UN_IDENTIFICATION::getException);

        postImageRepository.deleteAllByMissingPostId(missingPost.getId());
        List<PostImage> getPostImages = postImageRepository.findAllByMissingPostId(missingPost.getId());

        commentRepository.deleteAllByMissingPostId(missingPost.getId());
        List<Comment> getComments = commentRepository.findAllByMissingPostId(missingPost.getId());

        List<PostTag> getPostTags = postTagRepository.getPostTagsByMissingPostId(missingPost.getId());

        getPostTags.stream()
            .map(PostTag::getTag)
            .forEach(Tag::decreaseCount);
        Tag getTag = tagRepository.findById(tag.getId()).get();

        postTagRepository.deleteAllByMissingPostId(missingPost.getId());
        List<PostTag> getPostTagsAfterDelete = postTagRepository.findAll();

        missingPostRepository.deleteById(missingPost.getId());
        List<MissingPost> getMissingPosts = missingPostRepository.findAll();

        //then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(getMissingPost.getId()).isEqualTo(missingPost.getId());
            softAssertions.assertThat(getPostImages.isEmpty());
            softAssertions.assertThat(getComments.isEmpty());
            softAssertions.assertThat(getPostTags.size()).isEqualTo(1);
                softAssertions.assertThat(getTag.getCount()).isEqualTo(0);
                softAssertions.assertThat(getMissingPosts.size()).isEqualTo(0);
            }
        );
    }

}
