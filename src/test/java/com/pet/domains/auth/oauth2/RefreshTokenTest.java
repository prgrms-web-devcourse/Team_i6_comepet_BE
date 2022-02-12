package com.pet.domains.auth.oauth2;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RefreshTokenTest {

    @Test
    @DisplayName("30일이 경과한 토큰은 유효하지 않다.")
    void refreshToken() {
        RefreshToken token = RefreshToken.builder().token("token").build();
        LocalDateTime after = token.getCreatedAt().plusDays(30);

        assertTrue(token.isVerifyTokenByBefore30Days());
    }
}