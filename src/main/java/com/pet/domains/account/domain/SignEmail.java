package com.pet.domains.account.domain;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.BaseEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sign_email")
public class SignEmail extends BaseEntity {

    private static final Long EXPIRATION = 3L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "verify_key", nullable = false, unique = true)
    private String verifyKey;

    @Column(name = "is_checked", columnDefinition = "boolean default false")
    private boolean isChecked;

    @Builder
    public SignEmail(String email, String verifyKey) {
        this.email = email;
        this.verifyKey = verifyKey;
    }

    public boolean isVerifyTime(LocalDateTime requestTime) {
        return getCreatedAt().plusMinutes(EXPIRATION).isAfter(requestTime);
    }

    public void successVerified() {
        this.isChecked = true;
    }

    public boolean isVerifyEmail(String email) {
        if (StringUtils.equals(this.email, email) || isChecked) {
            return true;
        }
        throw ExceptionMessage.INVALID_SIGN_UP.getException();
    }
}
