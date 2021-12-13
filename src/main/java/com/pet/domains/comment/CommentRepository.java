package com.pet.domains.comment;

import com.pet.domains.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteAllByMissingPostId(Long postId);

    List<Comment> findAllByMissingPostId(Long postId);

}
