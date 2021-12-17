package com.pet.domains.account.controller;

import com.pet.common.exception.ExceptionMessage;
import com.pet.common.response.ApiResponse;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.domain.LoginAccount;
import com.pet.domains.account.dto.request.NotificationUpdateParam;
import com.pet.domains.account.dto.response.NotificationReadResults;
import com.pet.domains.account.service.NotificationService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
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
    public ApiResponse<NotificationReadResults> getNotifications(@LoginAccount Account account, Pageable pageable) {
        return ApiResponse.ok(notificationService.getByAccountId(pageable));
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
        if (Objects.isNull(account)) {
            throw ExceptionMessage.NOT_FOUND_JWT.getException();
        }
        notificationService.checkNotification(account, noticeId, param.isChecked());
    }

}
