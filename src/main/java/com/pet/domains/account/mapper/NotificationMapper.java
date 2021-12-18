package com.pet.domains.account.mapper;

import com.pet.domains.account.domain.Notification;
import com.pet.domains.account.dto.response.NotificationReadResults;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {


    @Mapping(target = "nickname", source = "account.nickname")
    @Mapping(target = "image", source = "missingPost.thumbnail")
    @Mapping(target = "postId", source = "missingPost.id")
    @Mapping(target = "status", source = "missingPost.status")
    @Mapping(target = "animalKindName", source = "missingPost.animalKind.name")
    @Mapping(target = "town", source = "missingPost.town.name")
    NotificationReadResults.Notification toNotificationDto(Notification notification);

}
