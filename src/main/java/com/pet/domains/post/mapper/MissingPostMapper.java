package com.pet.domains.post.mapper;

import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.image.domain.Image;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.response.MissingPostReadResult;
import com.pet.domains.post.dto.response.MissingPostReadResult.Account;
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
        @Mapping(target = "animalKind", source = "missingPostWithIsBookmark.missingPost.animalKind.animal.name"),
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
        @Mapping(target = "animalKind", source = "missingPost.animalKind.animal.name"),
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
        @Mapping(target = "id", source = "missingPost.id"),
        @Mapping(target = "account", source = "missingPost.account"),
        @Mapping(target = "status", source = "missingPost.status"),
        @Mapping(target = "date", source = "missingPost.date"),
        @Mapping(target = "city", source = "missingPost.town.city.name)"),
        @Mapping(target = "town", source = "missingPost.town.name)"),
        @Mapping(target = "detailAddress", source = "missingPost.detailAddress"),
        @Mapping(target = "telNumber", source = "missingPost.telNumber"),
        @Mapping(target = "animal", source = "missingPost.animalKind.animal.name"),
        @Mapping(target = "animalKind", source = "missingPost.animalKind.name"),
        @Mapping(target = "age", source = "missingPost.age"),
        @Mapping(target = "sex", source = "missingPost.sexType"),
        @Mapping(target = "chipNumber", source = "missingPost.chipNumber"),
//        @Mapping(target = "images", expression = "java(toMissingPostImageDto(missingPost.getImages))"),
        @Mapping(target = "tags", expression = "java(toMissingPostTagResult(missingPost.getPostTags()))"),
        @Mapping(target = "telNumber", source = "missingPost.telNumber"),
        @Mapping(target = "telNumber", source = "missingPost.telNumber")
    })
    MissingPostReadResult toMissingPostDto(MissingPost missingPost);

    @Mappings({
        @Mapping(target = "id", source = "account.id"),
        @Mapping(target = "nickname", source = "account.nickname"),
        @Mapping(target = "image", source = "account.image")
    })
    MissingPostReadResult.Account toMissingPostAccountDto(Account account);

    @Mappings({
        @Mapping(target = "id", source = "image.id"),
        @Mapping(target = "name", source = "image.name")
    })
    MissingPostReadResult.Image toMissingPostImageDto(Image image);

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

}
