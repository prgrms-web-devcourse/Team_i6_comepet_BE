package com.pet.domains.comment.repository;

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
import com.pet.domains.comment.domain.Comment;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.SexType;
import com.pet.domains.post.domain.Status;
import java.time.LocalDate;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = {JpaAuditingConfig.class, QuerydslConfig.class})
)
@DisplayName("Comment Repository 테스트")
class CommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentRepository commentRepository;

    private MissingPost missingPost;

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
        entityManager.persist(missingPost);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("게시글 아이디로 댓글 목록 삭제 테스트")
    void deleteAllByMissingPostIdTest() {
        // given
        LongStream.rangeClosed(1, 3).forEach(idx ->
            entityManager.persist(Comment.builder()
                .missingPost(missingPost)
                .content("내용")
                .account(account)
                .build()
            )
        );
        entityManager.flush();
        entityManager.clear();

        // when
        commentRepository.deleteAllByMissingPostId(missingPost.getId());
        List<Comment> comments = commentRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(comments).hasSize(3);
                comments.forEach(foundComment -> softAssertions.assertThat(foundComment.isDeleted()).isTrue());
            }
        );
    }

    @Test
    @DisplayName("댓글 아이디와 회원값으로 삭제 테스트")
    void deleteByIdAndAccountTest() {
        // given
        Comment comment = Comment.builder()
            .missingPost(missingPost)
            .account(account)
            .content("내용")
            .build();
        entityManager.persist(comment);
        entityManager.flush();
        entityManager.clear();

        // when
        commentRepository.deleteByIdAndAccount(comment.getId(), account);
        List<Comment> comments = commentRepository.findAll();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(comments).hasSize(1);
                comments.forEach(foundComment -> softAssertions.assertThat(foundComment.isDeleted()).isTrue());
            }
        );
    }

    @Test
    @DisplayName("댓글 아이디로 다른 엔티티 페치해서 가져오기")
    void findByIdWithFetchTest() {
        // given
        Comment comment = Comment.builder()
            .missingPost(missingPost)
            .account(account)
            .content("내용")
            .build();
        entityManager.persist(comment);
        entityManager.flush();
        entityManager.clear();

        // when
        Comment foundComment = commentRepository.findByIdWithFetch(comment.getId()).get();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(foundComment.getId()).isEqualTo(comment.getId());
            }
        );
    }

    @Test
    @DisplayName("댓글 목록 조회 테스트")
    void findAllWithFetchTest() {
        // given
        Comment parentComment = Comment.builder()
            .missingPost(missingPost)
            .account(account)
            .content("부모 댓글")
            .build();
        entityManager.persist(parentComment);

        Comment parentComment2 = Comment.builder()
            .missingPost(missingPost)
            .account(account)
            .content("부모 댓글2")
            .build();
        entityManager.persist(parentComment2);

        Comment childComment = Comment.ChildCommentBuilder()
            .missingPost(missingPost)
            .account(account)
            .parentComment(parentComment)
            .content("부모1의 대댓글 1")
            .build();
        entityManager.persist(childComment);

        Comment childComment2 = Comment.ChildCommentBuilder()
            .missingPost(missingPost)
            .account(account)
            .parentComment(parentComment)
            .content("부모1의 대댓글 2")
            .build();
        entityManager.persist(childComment2);
        entityManager.flush();
        entityManager.clear();

        // when
        Page<Comment> comments = commentRepository.findAllByMissingPostId(missingPost.getId(), PageRequest.of(0, 10));

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(comments.getTotalElements()).isEqualTo(2);
                softAssertions.assertThat(comments.isLast()).isTrue();
                softAssertions.assertThat(comments.getSize()).isEqualTo(10);
                softAssertions.assertThat(comments.getContent().get(0).getChildComments()).hasSize(2);
                softAssertions.assertThat(comments.getContent().get(1).getChildComments()).isEmpty();
            }
        );
    }

}
