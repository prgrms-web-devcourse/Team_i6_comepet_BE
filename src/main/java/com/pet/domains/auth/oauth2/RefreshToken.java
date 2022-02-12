package com.pet.domains.auth.oauth2;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue
    private Long id;

    private String token;

    private String email;

    private LocalDateTime createdAt;

    public RefreshToken() {}

    @Builder
    public RefreshToken(String token, String email) {
        this.token = token;
        this.email = email;
        createdAt = LocalDateTime.now();
    }

    public boolean isVerifyTokenByBefore30Days() {
        return createdAt.isAfter(LocalDateTime.now().plusDays(30));
    }
}
