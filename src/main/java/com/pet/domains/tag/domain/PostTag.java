package com.pet.domains.tag.domain;

import com.pet.domains.BaseEntity;
import com.pet.domains.post.domain.MissingPost;
import java.util.Objects;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
@Setter(AccessLevel.PRIVATE)
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(
        name = "tag_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_tag_to_post_tag"),
        nullable = false
    )
    private Tag tag;

    @Builder
    public PostTag(MissingPost missingPost, Tag tag) {
        ObjectUtils.requireNonEmpty(missingPost, "missingPost must not be null");
        ObjectUtils.requireNonEmpty(tag, "tag must not be null");

        addMissingPost(missingPost);
        this.tag = tag;
    }

    private void addMissingPost(MissingPost missingPost) {
        if (Objects.nonNull(this.missingPost)) {
            missingPost.getPostTags().remove(this);
        }
        this.missingPost = missingPost;
        missingPost.getPostTags().add(this);
    }

    private void removeMissingPost(MissingPost missingPost) {
        if (Objects.nonNull(this.missingPost)) {
            missingPost.getPostTags().remove(this);
        }
        this.missingPost = missingPost;
        missingPost.getPostTags().remove(this);
    }

}
