package com.pet.domains.account.mapper;

import com.pet.domains.account.domain.Notification;
import com.pet.domains.account.dto.response.NotificationReadResults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mappings({
        @Mapping(target = "nickname", source = "notification.account.nickname"),
        @Mapping(target = "image", source = "notification.missingPost.thumbnail"),
        @Mapping(target = "postId", source = "notification.missingPost.id"),
        @Mapping(target = "status", source = "notification.missingPost.status")
    })
    NotificationReadResults.Notification toNotificationDto(Notification notification);

}
