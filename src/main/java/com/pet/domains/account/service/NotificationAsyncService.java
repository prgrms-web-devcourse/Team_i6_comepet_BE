package com.pet.domains.account.service;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.Notification;
import com.pet.domains.account.repository.AccountRepository;
import com.pet.domains.account.repository.NotificationRepository;
import com.pet.domains.post.domain.MissingPost;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationAsyncService {

    private final NotificationRepository notificationRepository;

    private final AccountRepository accountRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void createNotifications(MissingPost triggerMissingPost, Long publisherAccountId) {
        log.info("start async createNotifications task");
        List<Account> subscribers = accountRepository.findAllByNotificationSubscribers(
            triggerMissingPost.getTown().getId(),
            publisherAccountId
        );
        List<Notification> notifications = subscribers.stream()
            .map(account -> Notification.builder()
                .account(account)
                .missingPost(triggerMissingPost)
                .build())
            .collect(Collectors.toList());

        notificationRepository.saveAll(notifications);
        log.info("complete async createNotifications task");
    }
}
