package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingPostRepository extends JpaRepository<MissingPost, Long> {

    void deleteMissingPostByIdAndAccount(Long postId, Account account);

}
