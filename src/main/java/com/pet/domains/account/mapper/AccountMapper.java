package com.pet.domains.account.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.response.AccountMissingPostPageResults;
import com.pet.domains.account.dto.response.AccountReadResult;
import com.pet.domains.post.domain.MissingPost;
import com.pet.domains.tag.domain.PostTag;
import com.pet.domains.tag.domain.Tag;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "image", source = "account.image.name")
    AccountReadResult toReadResult(Account account);

    @Mappings({
        @Mapping(target = "town", source = "missingPost.town.name"),
        @Mapping(target = "city", source = "missingPost.town.city.name"),
        @Mapping(target = "animalKind", source = "missingPost.animalKind.name"),
        @Mapping(target = "sex", source = "missingPost.sexType"),
        @Mapping(target = "postTags", expression = "java(toMissingPostTagResults(missingPost.getPostTags()))"),
    })
    AccountMissingPostPageResults.Post toAccountMissingPostPageResults(MissingPost missingPost);

    AccountMissingPostPageResults.Post.Tag toTagDto(Tag tag);

    default List<AccountMissingPostPageResults.Post.Tag> toMissingPostTagResults(List<PostTag> postTags) {
        return postTags.stream()
            .map(postTag -> toTagDto(postTag.getTag()))
            .collect(Collectors.toList());
    }

}
