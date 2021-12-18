package com.pet.domains.account.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByIdAndAccount(Long noticeId, Account account);

    Optional<Notification> findByIdAndAccount(Long noticeId, Account account);

    @EntityGraph(
        attributePaths = {"account", "missingPost", "missingPost.animalKind", "missingPost.town"},
        type = EntityGraph.EntityGraphType.LOAD
    )
    Page<Notification> findByAccount(Account account, Pageable pageable);

    void deleteAllByAccount(Account account);

}
