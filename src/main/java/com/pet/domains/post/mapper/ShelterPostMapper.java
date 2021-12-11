package com.pet.domains.post.mapper;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShelterPostMapper {

    @Mapping(target = "bookmarkCount", expression = "java(0)")
    @Mapping(target = "animalKind", source = "animalKindEntity")
    ShelterPost toEntity(
        ShelterPostCreateParams.ShelterPost param,
        AnimalKind animalKindEntity
    );

}
