package com.pet.domains.comment.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteAllByMissingPostId(Long postId);

    void deleteByIdAndAccount(Long commentId, Account account);

    List<Comment> findAllByMissingPostId(Long postId);

    @EntityGraph(attributePaths = {"account", "account.image", "childComments"}, type = EntityGraphType.LOAD)
    @Query("select distinct c from Comment c where c.missingPost.id = :postId and c.parentComment.id is null")
    Page<Comment> findAllWithFetch(Long postId, Pageable pageable);

}
