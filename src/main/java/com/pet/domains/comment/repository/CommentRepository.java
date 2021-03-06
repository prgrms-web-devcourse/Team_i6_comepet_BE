package com.pet.domains.comment.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"account", "missingPost"}, type = EntityGraphType.LOAD)
    @Query("select c from Comment c where c.id = :commentId and c.deleted = :deleted")
    Optional<Comment> findByIdAndDeletedWithFetch(Long commentId, boolean deleted);

    void deleteAllByMissingPostId(Long postId);

    void deleteByIdAndAccount(Long commentId, Account account);

    List<Comment> findAllByMissingPostId(Long postId);

    @EntityGraph(
        attributePaths = {"account", "account.image", "missingPost", "parentComment"},
        type = EntityGraphType.LOAD
    )
    @Query("select c from Comment c where c.missingPost.id = :postId and c.parentComment.id is null")
    Page<Comment> findAllByMissingPostId(Long postId, Pageable pageable);

}
