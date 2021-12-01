package com.pet.domains.comment.domain;

import com.pet.domains.DeletableEntity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comment SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
@Entity
@Table(name = "comment")
public class Comment extends DeletableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "parent_comment_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_child_comment_to_parent_comment"))
    private Comment parentComment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parentComment")
    private List<Comment> childComments = new ArrayList<>();

}
