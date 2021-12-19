package com.pet.domains.tag.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.domains.tag.domain.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest(includeFilters = @Filter(
    type = FilterType.ASSIGNABLE_TYPE,
    classes = {JpaAuditingConfig.class, QuerydslConfig.class})
)
@DisplayName("태그 리포지토리 테스트")
class TagRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @DisplayName("이름으로 tag 조회 테스트")
    void findTagByNameTest() {
        // given
        Tag tag = Tag.builder()
            .name("웰시코기")
            .build();
        entityManager.persist(tag);

        // when
        Tag foundTag = tagRepository.findTagByName("웰시코기").get();

        // then
        assertThat(foundTag.getId()).isEqualTo(tag.getId());
    }

}
