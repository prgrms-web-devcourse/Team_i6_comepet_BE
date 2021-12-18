package com.pet.domains.post.mapper;

import com.pet.domains.account.dto.response.AccountBookmarkPostPageResults;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface MissingPostMapper {

    @Mappings({
        @Mapping(target = "sexType", source = "param.sex"),
        @Mapping(target = "viewCount", expression = "java(0)"),
        @Mapping(target = "bookmarkCount", expression = "java(0)"),
        @Mapping(target = "commentCount", expression = "java(0)"),
        @Mapping(target = "town", source = "townEntity"),
        @Mapping(target = "animalKind", source = "animalKindEntity")
    })
    MissingPost toEntity(MissingPostCreateParam param, Town townEntity,
        AnimalKind animalKindEntity, String thumbnail, com.pet.domains.account.domain.Account account);

    @Mappings({
        @Mapping(target = "account", expression = "java(toMissingPostAccountDto(missingPost.getAccount()))"),
        @Mapping(target = "status", source = "missingPost.status"),
        @Mapping(target = "date", source = "missingPost.date"),
        @Mapping(target = "city", expression = "java(missingPost.getTown().getCity().getName())"),
        @Mapping(target = "town", expression = "java(missingPost.getTown().getName())"),
        @Mapping(target = "detailAddress", source = "missingPost.detailAddress"),
        @Mapping(target = "telNumber", source = "missingPost.telNumber"),
        @Mapping(target = "animal", source = "missingPost.animalKind.animal.name"),
        @Mapping(target = "animalKindName", source = "missingPost.animalKind.name"),
        @Mapping(target = "age", source = "missingPost.age"),
        @Mapping(target = "sex", source = "missingPost.sexType"),
        @Mapping(target = "chipNumber", source = "missingPost.chipNumber"),
        @Mapping(target = "images", expression = "java(toMissingPostImageResult(missingPost.getPostImages()))"),
        @Mapping(target = "tags", expression = "java(toMissingPostTagResult(missingPost.getPostTags()))"),
        @Mapping(target = "content", source = "missingPost.content"),
        @Mapping(target = "viewCount", source = "missingPost.viewCount"),
        @Mapping(target = "bookmarkCount", source = "missingPost.bookmarkCount"),
        @Mapping(target = "isBookmark", expression = "java(false)"),
        @Mapping(target = "createdAt", source = "missingPost.createdAt")
    })
    MissingPostReadResult toMissingPostDto(MissingPost missingPost);

    @Mappings({
        @Mapping(target = "animalKind", source = "missingPost.animalKind.name"),
        @Mapping(target = "thumbnail", source = "missingPost.thumbnail"),
        @Mapping(target = "place",
            expression = "java(joinPlace(missingPost.getTown().getName(), missingPost.getTown().getCity().getName()))"),
    })
    AccountBookmarkPostPageResults.Post toAccountBookmarkMissingPost(MissingPost missingPost);

    @Mappings({
        @Mapping(target = "id", source = "account.id"),
        @Mapping(target = "nickname", source = "account.nickname"),
        @Mapping(target = "image", source = "account.image.name")
    })
    MissingPostReadResult.Account toMissingPostAccountDto(com.pet.domains.account.domain.Account account);

    @Mappings({
        @Mapping(target = "id", source = "id"),
        @Mapping(target = "name", source = "image.name")
    })
    MissingPostReadResult.Image toMissingPostImageDto(PostImage postImage);

    @Mappings({
        @Mapping(target = "id", source = "tag.id"),
        @Mapping(target = "name", source = "tag.name")
    })
    MissingPostReadResult.Tag toMissingPostTagDto(Tag tag);

    default List<MissingPostReadResult.Tag> toMissingPostTagResult(List<PostTag> postTags) {
        return postTags.stream()
            .map(postTag -> toMissingPostTagDto(postTag.getTag()))
            .collect(Collectors.toList());
    }

    default String joinPlace(String city, String town) {
        return new StringBuilder().append(city).append(" ").append(town).toString();
    }

    default List<MissingPostReadResult.Image> toMissingPostImageResult(List<PostImage> postImages) {
        return postImages.stream()
            .map(this::toMissingPostImageDto)
            .collect(Collectors.toList());
    }

}
