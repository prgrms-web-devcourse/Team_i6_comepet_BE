package com.pet.domains.account.domain;

import com.pet.common.exception.ExceptionMessage;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("email")
public class Email implements Serializable {

    private static final Long EXPIRATION = 3L;

    @Id
    private String email;

    private String verifyKey;

    private boolean checked;

    private LocalDateTime createdAt;

    public Email(String email, String verifyKey) {
        this.email = email;
        this.verifyKey = verifyKey;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isVerifyTime(LocalDateTime requestTime) {
        return this.createdAt.plusMinutes(EXPIRATION).isAfter(requestTime);
    }

    public void successVerified() {
        this.checked = true;
    }

    public boolean isVerifyEmail(String email) {
        if (StringUtils.equals(this.email, email) && checked) {
            return true;
        }
        throw ExceptionMessage.INVALID_SIGN_UP.getException();
    }

    public boolean verify(String key) {
        return verifyKey.equals(key);
    }
}
