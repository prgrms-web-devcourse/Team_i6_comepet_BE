package com.pet.domains.account.mapper;

import com.pet.domains.account.domain.Notification;
import com.pet.domains.account.dto.response.NotificationReadResults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {


    @Mapping(target = "nickname", source = "notification.account.nickname")
    @Mapping(target = "image", source = "notification.missingPost.thumbnail")
    @Mapping(target = "postId", source = "notification.missingPost.id")
    @Mapping(target = "status", source = "notification.missingPost.status")
    @Mapping(target = "animalKindName", source = "notification.missingPost.animalKind.name")
    @Mapping(target = "town", source = "notification.missingPost.town.name")
    NotificationReadResults.Notification toNotificationDto(Notification notification);

}
