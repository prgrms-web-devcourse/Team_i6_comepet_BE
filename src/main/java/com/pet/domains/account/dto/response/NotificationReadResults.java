package com.pet.domains.account.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class NotificationReadResults {

    private final List<NotificationReadResults.Notification> notifications;

    public NotificationReadResults(List<NotificationReadResults.Notification> notifications) {
        this.notifications = notifications;
    }

    public static NotificationReadResults of(List<NotificationReadResults.Notification> notifications) {
        return new NotificationReadResults(notifications);
    }

    @Getter
    public static class Notification {

        private final String nickname;

        private final String image;

        private final Long postId;

        private final String status;

        public Notification(String nickname, String image, Long postId, String status) {
            this.nickname = nickname;
            this.image = image;
            this.postId = postId;
            this.status = status;
        }

        public static Notification of(String nickname, String image, Long postId, String status) {
            return new Notification(nickname, image, postId, status);
        }
    }

}
