package com.pet.domains.image.domain;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_image")
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "missing_post_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_missing_post_to_post_image"),
        nullable = false
    )
    private MissingPost missingPost;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "image_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_image_to_post_image"),
        nullable = false
    )
    private Image image;

    @Builder
    public PostImage(MissingPost missingPost, Image image) {
        ObjectUtils.requireNonEmpty(missingPost, "missingPost must not be null");
        ObjectUtils.requireNonEmpty(image, "image must not be null");

        this.missingPost = missingPost;
        this.image = image;
    }

}
