package com.pet.domains.account.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.response.AccountReadResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "image", source = "account.image.name")
    AccountReadResult toReadResult(Account account);

}
