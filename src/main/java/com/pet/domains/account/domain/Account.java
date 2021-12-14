package com.pet.domains.account.domain;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.DeletableEntity;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.image.domain.Image;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "account")
@AllArgsConstructor
public class Account extends DeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 100)
    private String nickname;

    @Column(name = "notification", columnDefinition = "boolean default false")
    private boolean notification;

    @Column(name = "checked_area", columnDefinition = "boolean default false")
    private boolean checkedArea;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_image_to_account")
    )
    private Image image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_group_to_account")
    )
    private Group group;

    @Column(name = "provider", length = 10)
    private String provider;

    @Builder
    public Account(String email, String password, String nickname, boolean notification, boolean checkedArea,
        Image image, Group group) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.notification = notification;
        this.checkedArea = checkedArea;
        this.image = image;
        this.group = group;
    }

    @Builder
    public Account(String email, String nickname, String provider, Image profileImage, Group group) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.image = profileImage;
        this.group = group;
    }

    public boolean isMatchPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

    public void updateNotification(boolean notification) {
        this.notification = notification;
    }

    public void isIdentification(Long accountId) {
        if (this.id.equals(accountId)) {
            throw ExceptionMessage.UN_IDENTIFICATION.getException();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Account account = (Account) obj;
        return id.equals(account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateProfile(String nickname, String password) {
        if (StringUtils.isNotBlank(nickname)) {
            this.nickname = nickname;
        }

        if (StringUtils.isNotBlank(password)) {
            this.password = password;
        }
    }

}
