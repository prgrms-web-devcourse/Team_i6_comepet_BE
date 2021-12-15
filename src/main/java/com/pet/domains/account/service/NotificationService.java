package com.pet.domains.account.service;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void deleteNoticeById(Account account, Long notificationId) {
        notificationRepository.deleteByIdAndAccount(notificationId, account);
    }
}
