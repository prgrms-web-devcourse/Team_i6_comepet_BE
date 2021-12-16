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

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"account", "missingPost"}, type = EntityGraphType.LOAD)
    Optional<Comment> findById(Long commentId);

    void deleteAllByMissingPostId(Long postId);

    void deleteByIdAndAccount(Long commentId, Account account);

    List<Comment> findAllByMissingPostId(Long postId);

    @EntityGraph(attributePaths = {"account", "account.image", "missingPost"}, type = EntityGraphType.LOAD)
    Page<Comment> findAllByMissingPostId(Long postId, Pageable pageable);

}
