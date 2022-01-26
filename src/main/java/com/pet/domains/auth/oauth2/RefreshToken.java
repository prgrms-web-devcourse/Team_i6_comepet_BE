package com.pet.domains.auth.oauth2;

import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("refreshToken")
public class RefreshToken {

    @Id
    private String token;

    private String email;

    private LocalDateTime createdAt;

    @Builder
    public RefreshToken(String token, String email) {
        this.token = token;
        this.email = email;
        createdAt = LocalDateTime.now();
    }

    public boolean isVerify() {
        return createdAt.isAfter(LocalDateTime.now().plusDays(30));
    }
}
