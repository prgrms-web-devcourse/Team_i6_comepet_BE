package com.pet.domains.account.domain;

import com.pet.domains.auth.domain.Group;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 테스트")
class AccountTest {

    @Test
    @DisplayName("비밀번호 일치 테스트")
    void checkPasswordTest() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Account account = Account.builder()
            .email("tester@email.com")
            .nickname("tester")
            .group(new Group("group"))
            .password(passwordEncoder.encode("123123a!"))
            .build();

        assertTrue(account.isMatchPassword(passwordEncoder, "123123a!"));
    }

}