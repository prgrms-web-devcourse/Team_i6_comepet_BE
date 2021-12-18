package com.pet.domains.post.mapper;

import com.pet.domains.account.dto.response.AccountBookmarkPostPageResults;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.image.domain.PostImage;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.post.repository.MissingPostWithIsBookmark;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.data.domain.Page;

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
        @Mapping(target = "town",
            expression = "java(String.valueOf(missingPostWithIsBookmark.getMissingPost().getTown().getName()))"),
        @Mapping(target = "city",
            expression = "java(String.valueOf("
                + "missingPostWithIsBookmark.getMissingPost().getTown().getCity().getName()))"),
        @Mapping(target = "animalKindName", source = "missingPostWithIsBookmark.missingPost.animalKind.name"),
        @Mapping(target = "status", source = "missingPostWithIsBookmark.missingPost.status"),
        @Mapping(target = "createdAt", source = "missingPostWithIsBookmark.missingPost.createdAt"),
        @Mapping(target = "sex", source = "missingPostWithIsBookmark.missingPost.sexType"),
        @Mapping(target = "bookmarkCount", source = "missingPostWithIsBookmark.missingPost.bookmarkCount"),
        @Mapping(target = "thumbnail", source = "missingPostWithIsBookmark.missingPost.thumbnail"),
        @Mapping(target = "isBookmark", source = "missingPostWithIsBookmark.isBookmark"),
        @Mapping(target = "tags",
            expression = "java(toMissingPostTagResults(missingPostWithIsBookmark.getMissingPost().getPostTags()))"),
        @Mapping(target = "id", source = "missingPostWithIsBookmark.missingPost.id")
    })
    MissingPostReadResults.MissingPost toMissingPostsDto(MissingPostWithIsBookmark missingPostWithIsBookmark);

    @Mappings({
        @Mapping(target = "town", expression = "java(String.valueOf(missingPost.getTown().getName()))"),
        @Mapping(target = "city", expression = "java(String.valueOf(missingPost.getTown().getCity().getName()))"),
        @Mapping(target = "animalKindName", source = "missingPost.animalKind.name"),
        @Mapping(target = "status", source = "missingPost.status"),
        @Mapping(target = "createdAt", source = "missingPost.createdAt"),
        @Mapping(target = "sex", source = "missingPost.sexType"),
        @Mapping(target = "bookmarkCount", source = "missingPost.bookmarkCount"),
        @Mapping(target = "thumbnail", source = "missingPost.thumbnail"),
        @Mapping(target = "isBookmark", expression = "java(false)"),
        @Mapping(target = "tags", expression = "java(toMissingPostTagResults(missingPost.getPostTags()))")
    })
    MissingPostReadResults.MissingPost toMissingPostsDto(MissingPost missingPost);

    @Mapping(target = "name", source = "tag.name")
    MissingPostReadResults.MissingPost.Tag toMissingPostsTagDto(Tag tag);

    @Mappings({
        @Mapping(target = "animalKind", source = "missingPost.animalKind.name"),
        @Mapping(target = "thumbnail", source = "missingPost.thumbnail"),
        @Mapping(target = "place",
            expression = "java(joinPlace(missingPost.getTown().getName(), missingPost.getTown().getCity().getName()))"),
    })
    AccountBookmarkPostPageResults.Post toAccountBookmarkMissingPost(MissingPost missingPost);

    default MissingPostReadResults toMissingPostsResults(Page<MissingPost> pageResult) {
        List<MissingPostReadResults.MissingPost> missingPostResults =
            pageResult.getContent().stream().map(this::toMissingPostsDto).collect(Collectors.toList());
        return MissingPostReadResults.of(
            missingPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    default MissingPostReadResults toMissingPostsWithBookmarkResults(Page<MissingPostWithIsBookmark> pageResult) {
        List<MissingPostReadResults.MissingPost> missingPostResults =
            pageResult.getContent().stream().map(this::toMissingPostsDto).collect(Collectors.toList());
        return MissingPostReadResults.of(
            missingPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    default List<MissingPostReadResults.MissingPost.Tag> toMissingPostTagResults(List<PostTag> postTags) {
        return postTags.stream()
            .map(postTag -> toMissingPostsTagDto(postTag.getTag()))
            .collect(Collectors.toList());
    }


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

    @Mappings({
        @Mapping(target = "account",
            expression = "java(toMissingPostAccountDto(missingPostWithIsBookmark.getMissingPost().getAccount()))"),
        @Mapping(target = "status", source = "missingPostWithIsBookmark.missingPost.status"),
        @Mapping(target = "date", source = "missingPostWithIsBookmark.missingPost.date"),
        @Mapping(target = "city",
            expression = "java(missingPostWithIsBookmark.getMissingPost().getTown().getCity().getName())"),
        @Mapping(target = "town", expression = "java(missingPostWithIsBookmark.getMissingPost().getTown().getName())"),
        @Mapping(target = "detailAddress", source = "missingPostWithIsBookmark.missingPost.detailAddress"),
        @Mapping(target = "telNumber", source = "missingPostWithIsBookmark.missingPost.telNumber"),
        @Mapping(target = "animal", source = "missingPostWithIsBookmark.missingPost.animalKind.animal.name"),
        @Mapping(target = "animalKindName", source = "missingPostWithIsBookmark.missingPost.animalKind.name"),
        @Mapping(target = "age", source = "missingPostWithIsBookmark.missingPost.age"),
        @Mapping(target = "sex", source = "missingPostWithIsBookmark.missingPost.sexType"),
        @Mapping(target = "chipNumber", source = "missingPostWithIsBookmark.missingPost.chipNumber"),
        @Mapping(target = "images",
            expression = "java(toMissingPostImageResult(missingPostWithIsBookmark.getMissingPost().getPostImages()))"),
        @Mapping(target = "tags",
            expression = "java(toMissingPostTagResult(missingPostWithIsBookmark.getMissingPost().getPostTags()))"),
        @Mapping(target = "content", source = "missingPostWithIsBookmark.missingPost.content"),
        @Mapping(target = "viewCount", source = "missingPostWithIsBookmark.missingPost.viewCount"),
        @Mapping(target = "bookmarkCount", source = "missingPostWithIsBookmark.missingPost.bookmarkCount"),
        @Mapping(target = "isBookmark", source = "missingPostWithIsBookmark.isBookmark"),
        @Mapping(target = "createdAt", source = "missingPostWithIsBookmark.missingPost.createdAt"),
        @Mapping(target = "commentCount", source = "missingPostWithIsBookmark.missingPost.commentCount"),
        @Mapping(target = "id", source = "missingPostWithIsBookmark.missingPost.id")
    })
    MissingPostReadResult toMissingPostDto(MissingPostWithIsBookmark missingPostWithIsBookmark);

}
