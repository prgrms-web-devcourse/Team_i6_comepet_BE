package com.pet.domains.post.mapper;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.repository.ShelterPostWithIsBookmark;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface ShelterPostMapper {

    @Mapping(target = "bookmarkCount", expression = "java(0)")
    @Mapping(target = "animalKind", source = "animalKindEntity")
    @Mapping(target = "town", source = "townEntity")
    ShelterPost toEntity(
        ShelterPostCreateParams.ShelterPost param,
        AnimalKind animalKindEntity,
        Town townEntity
    );

    default ShelterPostPageResults toShelterPostPageResults(Page<ShelterPostWithIsBookmark> pageResult) {
        var shelterPostResults = pageResult.getContent().stream()
            .map(shelterPostWithIsBookmark -> {
                ShelterPost shelterPost = shelterPostWithIsBookmark.getShelterPost();
                return toShelterPostOfShelterPostPageResults(shelterPost, shelterPostWithIsBookmark.getIsBookmark());
            })
            .collect(Collectors.toList());
        return ShelterPostPageResults.of(
            shelterPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    @Mapping(target = "animalKind", source = "shelterPost.animalKind.name")
    @Mapping(target = "animal", source = "shelterPost.animalKind.animal.name")
    @Mapping(target = "isBookmark", source = "isBookmark")
    @Mapping(target = "town", source = "shelterPost.town.name")
    @Mapping(target = "city", source = "shelterPost.town.city.name")
    ShelterPostPageResults.ShelterPost toShelterPostOfShelterPostPageResults(
        ShelterPost shelterPost,
        boolean isBookmark);

}
