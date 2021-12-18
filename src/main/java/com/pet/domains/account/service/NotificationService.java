package com.pet.domains.account.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
import com.pet.domains.account.dto.response.NotificationReadResults;
import com.pet.domains.account.mapper.NotificationMapper;
import com.pet.domains.account.repository.NotificationRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final NotificationMapper notificationMapper;

    @Transactional
    public void deleteNoticeById(Account account, Long notificationId) {
        notificationRepository.deleteByIdAndAccount(notificationId, account);
    }

    @Transactional
    public void checkNotification(Account account, Long noticeId, boolean checked) {
        if (checked) {
            Notification notification = notificationRepository.findByIdAndAccount(noticeId, account)
                .orElseThrow(ExceptionMessage.NOT_FOUND_NOTIFICATION::getException);
            notification.checkReadStatus();
        }
    }

    public NotificationReadResults getByAccountId(Account account, Pageable pageable) {
        Page<Notification> notifications = notificationRepository.findByAccount(account, pageable);
        return NotificationReadResults.of(
            notifications.stream()
                .map(notificationMapper::toNotificationDto)
                .collect(Collectors.toList()),
            notifications.getTotalElements(),
            notifications.isLast(),
            notifications.getSize()
        );
    }
}
