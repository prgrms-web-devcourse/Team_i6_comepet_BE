package com.pet.domains.account.domain;

import com.pet.domains.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sign_email")
public class SignEmail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, length = 50)
    private String email;

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    public SignEmail(String email, String key) {
        this.email = email;
        this.key = key;
    }

}
