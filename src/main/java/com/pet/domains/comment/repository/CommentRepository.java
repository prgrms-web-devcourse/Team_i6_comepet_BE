package com.pet.domains.comment.repository;

import com.pet.domains.comment.domain.Comment;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"account", "missingPost"}, type = EntityGraphType.LOAD)
    Optional<Comment> findById(Long commentId);

    void deleteAllByMissingPostId(Long postId);

    List<Comment> findAllByMissingPostId(Long postId);

}
