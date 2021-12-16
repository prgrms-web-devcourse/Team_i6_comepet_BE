package com.pet.domains.account.controller;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.account.dto.request.NotificationUpdateParam;
import com.pet.domains.account.dto.response.NotificationReadResults;
import com.pet.domains.account.service.NotificationService;
import com.pet.domains.post.domain.Status;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<NotificationReadResults> getNotifications() {
        return ApiResponse.ok(NotificationReadResults.of(
            List.of(
                NotificationReadResults.Notification.of(
                    "고양이가 멍멍",
                    "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                    513L,
                    Status.DETECTION.name()
                ),
                NotificationReadResults.Notification.of(
                    "야옹이가 멍멍",
                    "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                    234L,
                    Status.DETECTION.name()
                ),
                NotificationReadResults.Notification.of(
                    "나홀로 집사",
                    "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                    1231L,
                    Status.DETECTION.name()
                )
            )
        ));
    }

    @DeleteMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(@LoginAccount Account account, @PathVariable Long noticeId) {
        notificationService.deleteNoticeById(account, noticeId);
    }

    @PatchMapping("/{noticeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void checkedNotification(
        @LoginAccount Account account, @PathVariable Long noticeId, @RequestBody NotificationUpdateParam param
    ) {
        if (Objects.nonNull(account)) {
            notificationService.checkNotification(account, noticeId, param.isChecked());
        }
        throw ExceptionMessage.NOT_FOUND_JWT.getException();
    }

}
