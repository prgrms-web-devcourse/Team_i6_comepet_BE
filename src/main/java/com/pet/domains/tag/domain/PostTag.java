package com.pet.domains.tag.domain;

import com.pet.domains.BaseEntity;
import com.pet.domains.post.domain.MissingPost;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_tag")
public class PostTag extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "missing_post_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_missing_post_to_post_tag"),
        nullable = false
    )
    private MissingPost missingPost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "tag_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_tag_to_post_tag"),
        nullable = false
    )
    private Tag tag;

}
