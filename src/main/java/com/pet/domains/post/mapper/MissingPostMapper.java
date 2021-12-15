package com.pet.domains.post.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.area.domain.Town;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.post.dto.request.MissingPostCreateParam;
import com.pet.domains.post.dto.response.MissingPostReadResults;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import java.util.ArrayList;
import java.util.List;
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

    default MissingPostReadResults toMissingPostResults(Page<MissingPost> pageResult) {
        List<MissingPostReadResults.MissingPost> missingPostResults = new ArrayList<>();
        for (MissingPost missingPost : pageResult.getContent()) {
            MissingPostReadResults.MissingPost post = toMissingPostDto(missingPost);
            missingPostResults.add(post);
        }
        return MissingPostReadResults.of(
            missingPostResults,
            pageResult.getTotalElements(),
            pageResult.isLast(),
            pageResult.getSize()
        );
    }

    //toMissingPostDto - 익명 사용자 리스트 조회
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

    default List<MissingPostReadResults.MissingPost.Tag> toMissingPostTagResults(List<PostTag> postTags) {
        List<MissingPostReadResults.MissingPost.Tag> missingPostTagResults = new ArrayList<>();
        //tag -> missingPost
        for (PostTag postTag : postTags) {
            MissingPostReadResults.MissingPost.Tag getTag = toMissingPostTagDto(postTag.getTag());
            missingPostTagResults.add(getTag);
        }
        return missingPostTagResults;
    }

    //toMissingPostTagsDto - 리스트 안 게시물의 태그 리스트 조회
    @Mappings({
        @Mapping(target = "name", source = "tag.name")
    })
    MissingPostReadResults.MissingPost.Tag toMissingPostTagDto(Tag tag);
}
