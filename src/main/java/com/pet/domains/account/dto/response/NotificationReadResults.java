package com.pet.domains.account.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class NotificationReadResults {

    private final List<NotificationReadResults.Notification> notifications;

    private final long totalElements;

    private final boolean last;

    private final long size;

    public NotificationReadResults(List<Notification> notifications, long totalElements, boolean last, long size) {
        this.notifications = notifications;
        this.totalElements = totalElements;
        this.last = last;
        this.size = size;
    }

    public static NotificationReadResults of(List<NotificationReadResults.Notification> notifications,
        long totalElements, boolean last, long size
    ) {
        return new NotificationReadResults(notifications, totalElements, last, size);
    }

    @Getter
    public static class Notification {

        private final String nickname;

        private final String image;

        private final Long postId;

        private final String status;

        private final String animalKindName;

        private final String town;

        private final boolean checked;

        public Notification(String nickname, String image, Long postId, String status, String animalKindName,
            String town, boolean checked) {
            this.nickname = nickname;
            this.image = image;
            this.postId = postId;
            this.status = status;
            this.animalKindName = animalKindName;
            this.town = town;
            this.checked = checked;
        }

        public static Notification of(String nickname, String image, Long postId, String status, String animalKindName,
            String town, boolean checked) {
            return new Notification(nickname, image, postId, status, animalKindName, town, checked);
        }
    }

}
