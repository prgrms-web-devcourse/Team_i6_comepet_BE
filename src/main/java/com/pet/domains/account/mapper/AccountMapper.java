package com.pet.domains.account.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.response.AccountMissingPostPageResults;
import com.pet.domains.account.dto.response.AccountReadResult;
import com.pet.domains.post.domain.MissingPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "image", source = "account.image.name")
    AccountReadResult toReadResult(Account account);

    @Mappings({
        @Mapping(target = "city", source = "missingPost.town.name"),
        @Mapping(target = "town", source = "missingPost.town.city.name"),
        @Mapping(target = "animalKind", source = "missingPost.animalKind.name"),
    })
    AccountMissingPostPageResults.Post toAccountMissingPostPageResults(MissingPost missingPost);

}
