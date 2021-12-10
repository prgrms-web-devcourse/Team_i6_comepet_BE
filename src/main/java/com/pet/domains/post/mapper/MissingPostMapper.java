package com.pet.domains.post.mapper;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MissingPostMapper {

    MissingPostMapper INSTANCE = Mappers.getMapper(MissingPostMapper.class);

    @Mappings({
        @Mapping(target = "id", ignore = true),
        @Mapping(target = "status", source = "param.status"),
        @Mapping(target = "detailAddress", source = "param.detailAddress"),
        @Mapping(target = "date", source = "param.date"),
        @Mapping(target = "age", source = "param.age"),
        @Mapping(target = "sexType", source = "param.sex"),
        @Mapping(target = "chipNumber", source = "param.chipNumber"),
        @Mapping(target = "content", source = "param.content"),
        @Mapping(target = "telNumber", source = "param.telNumber"),
        @Mapping(target = "viewCount", expression = "java(0)"),
        @Mapping(target = "bookmarkCount", expression = "java(0)"),
        @Mapping(target = "commentCount", expression = "java(0)"),
        @Mapping(target = "thumbnail", source = "thumbnail"),
        @Mapping(target = "account", expression = "java(null)"),
        @Mapping(target = "town", source = "townEntity"),
        @Mapping(target = "animalKind", source = "animalKindEntity")
    })
    MissingPost toEntity(MissingPostCreateParam param, Town townEntity,
        AnimalKind animalKindEntity, String thumbnail);

}
