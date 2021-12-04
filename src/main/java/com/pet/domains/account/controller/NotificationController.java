package com.pet.domains.account.controller;

import com.pet.common.response.ApiResponse;
import com.pet.domains.account.dto.request.AccountEmailParam;
import com.pet.domains.account.dto.response.NotificationReadResults;
import com.pet.domains.post.domain.Status;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<NotificationReadResults> getNotifications() {
        return ApiResponse.ok(NotificationReadResults.of(
            List.of(
                NotificationReadResults.Notification.of(
                    "고양이가 멍멍",
                    "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                    513L,
                    Status.DETECTION.getMeaning()
                ),
                NotificationReadResults.Notification.of(
                    "야옹이가 멍멍",
                    "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                    234L,
                    Status.DETECTION.getMeaning()
                ),
                NotificationReadResults.Notification.of(
                    "나홀로 집사",
                    "http://../../97fd3403-7343-497a-82fa-c41d26ccf0f8.png",
                    1231L,
                    Status.DETECTION.getMeaning()
                )
            )
        ));
    }

}
