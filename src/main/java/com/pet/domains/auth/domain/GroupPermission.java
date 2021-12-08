package com.pet.domains.auth.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "group_permission")
public class GroupPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_group_to_group_permission"),
        nullable = false
    )
    private Group group;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "permission_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_permission_to_group_permission"),
        nullable = false
    )
    private Permission permission;

    public GroupPermission(Group group, Permission permission) {
        this.group = group;
        this.permission = permission;
    }

}
