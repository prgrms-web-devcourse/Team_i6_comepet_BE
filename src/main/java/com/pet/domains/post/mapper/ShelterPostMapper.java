package com.pet.domains.post.mapper;

import com.pet.domains.account.dto.response.AccountBookmarkPostPageResults;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.domain.ShelterPost;
import com.pet.domains.post.dto.request.ShelterPostCreateParams;
import com.pet.domains.post.dto.response.ShelterPostPageResults;
import com.pet.domains.post.dto.response.ShelterPostReadResult;
import com.pet.domains.post.repository.projection.ShelterPostWithIsBookmark;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
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

    default ShelterPostPageResults toShelterPostPageResultsWithAccount(Page<ShelterPostWithIsBookmark> pageResult) {
        List<ShelterPostPageResults.ShelterPost> shelterPostResults = pageResult.getContent().stream()
            .map(shelterPostWithIsBookmark -> toShelterPagePostDto(
                shelterPostWithIsBookmark.getShelterPost(),
                shelterPostWithIsBookmark.getAnimalKind(),
                shelterPostWithIsBookmark.getAnimal(),
                shelterPostWithIsBookmark.getTown(),
                shelterPostWithIsBookmark.getCity(),
                shelterPostWithIsBookmark.isBookmark()))
            .collect(Collectors.toList());

        return ShelterPostPageResults.of(
            shelterPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    default ShelterPostPageResults toShelterPostPageResults(Page<ShelterPost> pageResult) {
        List<ShelterPostPageResults.ShelterPost> shelterPostResults = pageResult.getContent().stream()
            .map(this::toShelterPagePostDto)
            .collect(Collectors.toList());

        return ShelterPostPageResults.of(
            shelterPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    @Mapping(target = "id", source = "shelterPost.id")
    @Mapping(target = "animalKindName", source = "animalKind.name")
    @Mapping(target = "animal", source = "animal.name")
    @Mapping(target = "town", source = "town.name")
    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "isBookmark", source = "isBookmark")
    ShelterPostPageResults.ShelterPost toShelterPagePostDto(
        ShelterPost shelterPost,
        AnimalKind animalKind,
        Animal animal,
        Town town,
        City city,
        boolean isBookmark
    );

    @Mapping(target = "animalKindName", source = "shelterPost.animalKind.name")
    @Mapping(target = "animal", source = "shelterPost.animalKind.animal.name")
    @Mapping(target = "town", source = "shelterPost.town.name")
    @Mapping(target = "city", source = "shelterPost.town.city.name")
    @Mapping(target = "isBookmark", expression = "java(false)")
    ShelterPostPageResults.ShelterPost toShelterPagePostDto(ShelterPost shelterPost);

    @Mapping(target = "id", source = "shelterPost.id")
    @Mapping(target = "animalKindName", source = "animalKind.name")
    @Mapping(target = "animal", source = "animal.name")
    @Mapping(target = "town", source = "town.name")
    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "isBookmark", source = "isBookmark")
    @Mapping(target = "status", source = "shelterPost.postStatus")
    ShelterPostReadResult toShelterPostReadResult(
        ShelterPost shelterPost,
        AnimalKind animalKind,
        Animal animal,
        Town town,
        City city,
        boolean isBookmark
    );

    @Mapping(target = "animalKindName", source = "animalKind.name")
    @Mapping(target = "animal", source = "animalKind.animal.name")
    @Mapping(target = "town", source = "town.name")
    @Mapping(target = "city", source = "town.city.name")
    @Mapping(target = "isBookmark", expression = "java(false)")
    @Mapping(target = "status", source = "postStatus")
    ShelterPostReadResult toShelterPostReadResult(ShelterPost shelterPost);

    @Mappings({
        @Mapping(target = "animalKind", source = "shelterPost.animalKind.name"),
        @Mapping(target = "thumbnail", source = "shelterPost.thumbnail"),
        @Mapping(target = "sexType", source = "shelterPost.sex"),
        @Mapping(target = "city", source = "shelterPost.town.city.name"),
        @Mapping(target = "town", source = "shelterPost.town.name")
    })
    AccountBookmarkPostPageResults.Post toAccountBookmarkShelterPost(ShelterPost shelterPost);

}
