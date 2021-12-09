package com.pet.domains.account.dto;

import com.pet.domains.account.dto.request.AccountEmailCheck;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

public class AccountValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("이메일 정상 입력 테스트")
    void checkEmailTest() {
        AccountEmailCheck emailCheck = new AccountEmailCheck("test@test.com");
        Set<ConstraintViolation<AccountEmailCheck>> validations = validator.validate(emailCheck);
        assertThat(validations).hasSize(0);
    }

    @Test
    @DisplayName("이메일 비정상 입력 테스트 - test@test")
    void checkEmailFailNotContainsDotTest() {
        AccountEmailCheck emailCheck = new AccountEmailCheck("test@test");
        Set<ConstraintViolation<AccountEmailCheck>> validations = validator.validate(emailCheck);
        assertThat(validations).hasSize(1);

        assertThat(validations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList())).containsAnyOf("올바른 이메일 형식이 아닙니다.");
    }

    @Test
    @DisplayName("이메일 비정상 입력 테스트 - test.com")
    void checkEmailFailNotContainsAtTest() {
        AccountEmailCheck emailCheck = new AccountEmailCheck("test@test");
        Set<ConstraintViolation<AccountEmailCheck>> validations = validator.validate(emailCheck);
        assertThat(validations).hasSize(1);

        assertThat(validations.stream()
            .map(ConstraintViolation::getMessage)
            .collect(Collectors.toList())).containsAnyOf("올바른 이메일 형식이 아닙니다.");
    }

}
