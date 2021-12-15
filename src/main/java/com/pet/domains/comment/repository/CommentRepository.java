package com.pet.domains.comment.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.comment.domain.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    void deleteAllByMissingPostId(Long postId);

    void deleteByIdAndAccount(Long commentId, Account account);

    List<Comment> findAllByMissingPostId(Long postId);

}
