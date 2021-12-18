package com.pet.domains.post.mapper;

import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.post.repository.projection.MissingPostWithIsBookmarkNew;
import com.pet.domains.tag.domain.PostTag;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface MissingPostReadResultMapper {

    default MissingPostReadResults toMissingPostPageResultsWithAccount(Page<MissingPostWithIsBookmarkNew> pageResult) {
        List<MissingPostReadResults.MissingPost> missingPosts = pageResult.getContent().stream()
            .map(missingPostWithIsBookmark -> toMissingPagePostDtoWithIsBookmark(
                missingPostWithIsBookmark.getMissingPost(),
                missingPostWithIsBookmark.getAnimalKind(),
                missingPostWithIsBookmark.getAnimal(),
                missingPostWithIsBookmark.getTown(),
                missingPostWithIsBookmark.getCity(),
                missingPostWithIsBookmark.isBookmark(),
                missingPostWithIsBookmark.getMissingPost().getPostTags()
            ))
            .collect(Collectors.toList());

        return MissingPostReadResults.of(
            missingPosts,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    @Mapping(target = "id", source = "missingPost.id")
    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "town", source = "town.name")
    @Mapping(target = "animalKindName", source = "animalKind.name")
    @Mapping(target = "status", source = "missingPost.status")
    @Mapping(target = "createdAt", source = "missingPost.createdAt")
    @Mapping(target = "sex", source = "missingPost.sexType")
    @Mapping(target = "isBookmark", source = "isBookmark")
    @Mapping(target = "bookmarkCount", source = "missingPost.bookmarkCount")
    @Mapping(target = "thumbnail", source = "missingPost.thumbnail")
    @Mapping(target = "tags", source = "postTags")
    MissingPostReadResults.MissingPost toMissingPagePostDtoWithIsBookmark(
        MissingPost missingPost,
        AnimalKind animalKind,
        Animal animal,
        Town town,
        City city,
        boolean isBookmark,
        List<PostTag> postTags
    );

    @Mapping(target = "name", source = "postTag.tag.name")
    MissingPostReadResults.MissingPost.Tag toMissingPostReadResultsTag(PostTag postTag);


    default MissingPostReadResults toMissingPostPageResults(Page<com.pet.domains.post.domain.MissingPost> pageResult) {
        List<MissingPostReadResults.MissingPost> missingPosts = pageResult.getContent().stream()
            .map(this::toMissingPagePostDto)
            .collect(Collectors.toList());

        return MissingPostReadResults.of(
            missingPosts,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    @Mapping(target = "city", source = "town.city.name")
    @Mapping(target = "town", source = "town.name")
    @Mapping(target = "animalKindName", source = "animalKind.name")
    @Mapping(target = "sex", source = "sexType")
    @Mapping(target = "isBookmark", expression = "java(false)")
    @Mapping(target = "tags", source = "postTags")
    MissingPostReadResults.MissingPost toMissingPagePostDto(MissingPost missingPost);


    @Mapping(target = "id", source = "missingPost.id")
    @Mapping(target = "account.image", source = "missingPost.account.image.name")
    @Mapping(target = "city", source = "city.name")
    @Mapping(target = "town", source = "town.name")
    @Mapping(target = "animalKindName", source = "animalKind.name")
    @Mapping(target = "animal", source = "animal.name")
    @Mapping(target = "status", source = "missingPost.status")
    @Mapping(target = "createdAt", source = "missingPost.createdAt")
    @Mapping(target = "sex", source = "missingPost.sexType")
    @Mapping(target = "isBookmark", source = "isBookmark")
    @Mapping(target = "bookmarkCount", source = "missingPost.bookmarkCount")
    @Mapping(target = "tags", source = "postTags")
    @Mapping(target = "images", source = "postImages")
    MissingPostReadResult toMissingPostReadResult(
        MissingPost missingPost,
        AnimalKind animalKind,
        Animal animal,
        Town town,
        City city,
        boolean isBookmark,
        List<PostTag> postTags,
        List<PostImage> postImages
    );

    @Mapping(target = "name", source = "postTag.tag.name")
    MissingPostReadResult.Tag toMissingPostReadResultTag(PostTag postTag);

    @Mapping(target = "name", source = "postImage.image.name")
    MissingPostReadResult.Image toMissingPostReadResultImage(PostImage postImage);

}
