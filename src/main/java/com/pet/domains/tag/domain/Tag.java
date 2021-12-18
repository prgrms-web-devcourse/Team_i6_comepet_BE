package com.pet.domains.tag.domain;

import com.pet.domains.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "tag")
public class Tag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "count", columnDefinition = "BIGINT default 0", nullable = false)
    private long count;

    @Version
    private long version;

    @Builder
    public Tag(String name, long count) {
        ObjectUtils.requireNonEmpty(name, "name must not be null");
        ObjectUtils.requireNonEmpty(count, "count must not be null");

        this.name = name;
        this.count = count;
    }

    public void increaseCount() {
        this.count += 1;
    }

    public void decreaseCount() {
        if (this.count > 0) {
            this.count -= 1;
        }
    }

}
