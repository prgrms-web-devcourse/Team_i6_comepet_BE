package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.MissingPostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MissingPostBookmarkRepository extends JpaRepository<MissingPostBookmark, Long> {

    void deleteByMissingPostIdAndAccount(Long postId, Account account);

}
