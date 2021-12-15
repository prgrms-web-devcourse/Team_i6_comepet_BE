package com.pet.domains.account.repository;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    void deleteByIdAndAccount(Long noticeId, Account account);

}