package com.pet.domains.account.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
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

    @Transactional
    public void checkNotification(Account account, Long noticeId, boolean checked) {
        if (checked) {
            Notification notification = notificationRepository.findByIdAndAccountId(noticeId, account.getId())
                .orElseThrow(ExceptionMessage.NOT_FOUND_NOTIFICATION::getException);
            notification.checkReadStatus();
        }
    }
}
