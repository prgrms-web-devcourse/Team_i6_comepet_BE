package com.pet.domains.post.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.post.domain.ShelterPostBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterPostBookmarkRepository extends JpaRepository<ShelterPostBookmark, Long> {

    void deleteByShelterPostIdAndAccount(Long postId, Account account);
}
