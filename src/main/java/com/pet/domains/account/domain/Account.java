package com.pet.domains.account.domain;

import com.pet.domains.DeletableEntity;
import com.pet.domains.auth.domain.Group;
import com.pet.domains.image.domain.Image;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = "nickname", length = 10)
    private String nickname;

    @Column(name = "notification", columnDefinition = "boolean default false")
    private boolean notification;

    @Column(name = "checked_area", columnDefinition = "boolean default false")
    private boolean checkedArea;

    private SignStatus signStatus;

    @OneToOne(fetch = FetchType.LAZY)
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

    @Builder
    public Account(String email, String password, String nickname, boolean notification, boolean checkedArea,
        SignStatus signStatus, Image image, Group group) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.notification = notification;
        this.checkedArea = checkedArea;
        this.signStatus = signStatus;
        this.image = image;
        this.group = group;
    }

    public boolean isMatchPassword(PasswordEncoder passwordEncoder, String password) {
        return passwordEncoder.matches(password, this.password);
    }

}
