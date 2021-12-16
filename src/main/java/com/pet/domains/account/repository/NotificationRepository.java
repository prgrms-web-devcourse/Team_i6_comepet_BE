package com.pet.domains.account.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByIdAndAccount(Long noticeId, Account account);

    Optional<Notification> findByIdAndAccount(Long noticeId, Account account);

    @EntityGraph(attributePaths = {"account", "missingPost"}, type = EntityGraphType.LOAD)
    Page<Notification> findAll(Pageable pageable);

}
