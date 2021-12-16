package com.pet.domains.account.repository;

import static org.assertj.core.api.Assertions.assertThat;
import com.pet.common.config.JpaAuditingConfig;
import com.pet.common.config.QuerydslConfig;
import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.SignEmail;
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
@DisplayName("인증 이메일 DB 테스트")
class SignEmailRepositoryTest {

    @Autowired
    private SignEmailRepository signEmailRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("인증된 이메일 하나만 조회")
    void getVerifyEmail() {
        String email = "test@email.com";
        SignEmail s1 = SignEmail.builder().email(email).verifyKey("1234").build();
        SignEmail s2 = SignEmail.builder().email(email).verifyKey("12").build();
        SignEmail s3 = SignEmail.builder().email(email).verifyKey("4321").build();
        SignEmail s4 = SignEmail.builder().email(email).verifyKey("54321").build();
        s3.successVerified();

        signEmailRepository.save(s1);
        signEmailRepository.save(s2);
        signEmailRepository.save(s3);
        signEmailRepository.save(s4);

        entityManager.flush();
        entityManager.clear();

        signEmailRepository.deleteById(
            signEmailRepository.findByEmailAndIsCheckedTrue(email)
                .orElseThrow(ExceptionMessage.INVALID_SIGN_UP::getException).getId());

        assertThat(signEmailRepository.findAll().size()).isEqualTo(3);

    }
}
