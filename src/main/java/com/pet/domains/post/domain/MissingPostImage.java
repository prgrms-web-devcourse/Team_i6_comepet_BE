package com.pet.domains.post.domain;

import com.pet.domains.BaseEntity;
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "missing_post_image")
public class MissingPostImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "missing_post_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_missing_to_missing_post_image"),
        nullable = false
    )
    private MissingPost missingPost;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_image_to_missing_post_image"))
    private Image image;

}
