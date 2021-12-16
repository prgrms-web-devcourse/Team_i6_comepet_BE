package com.pet.domains.post.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
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
        AnimalKind animalKindEntity, String thumbnail, Account account);

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
    MissingPostReadResults.MissingPost toMissingPostDto(MissingPostWithIsBookmark missingPostWithIsBookmark);

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
    MissingPostReadResults.MissingPost toMissingPostDto(MissingPost missingPost);

    @Mappings({
        @Mapping(target = "name", source = "tag.name")
    })
    MissingPostReadResults.MissingPost.Tag toMissingPostTagDto(Tag tag);

    default MissingPostReadResults toMissingPostResults(Page<MissingPost> pageResult) {
        List<MissingPostReadResults.MissingPost> missingPostResults =
            pageResult.getContent().stream().map(this::toMissingPostDto).collect(Collectors.toList());
        return MissingPostReadResults.of(
            missingPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    default MissingPostReadResults toMissingPostWithBookmarkResults(Page<MissingPostWithIsBookmark> pageResult) {
        List<MissingPostReadResults.MissingPost> missingPostResults =
            pageResult.getContent().stream().map(this::toMissingPostDto).collect(Collectors.toList());
        return MissingPostReadResults.of(
            missingPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    default List<MissingPostReadResults.MissingPost.Tag> toMissingPostTagResults(List<PostTag> postTags) {
        return postTags.stream()
            .map(postTag -> toMissingPostTagDto(postTag.getTag()))
            .collect(Collectors.toList());
    }

}
