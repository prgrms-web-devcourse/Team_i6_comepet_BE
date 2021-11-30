package com.pet.domains.account.domain;

import com.pet.domains.DeletableEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE account SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "account")
public class Account extends DeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "nickname", nullable = false, length = 10)
    private String nickname;

    @Column(name = "notification", nullable = false, columnDefinition = "boolean default false")
    private Boolean notification;

    @Column(name = "checked_area", nullable = false, columnDefinition = "boolean default false")
    private Boolean checkedArea;

    @Column(name = "image")
    private String image;

}
