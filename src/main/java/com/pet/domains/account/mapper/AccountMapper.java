package com.pet.domains.account.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.response.AccountReadResult;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    AccountReadResult toReadResult(Account account);

}
