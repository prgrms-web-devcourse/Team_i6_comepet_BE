package com.pet.domains.area.mapper;

import com.pet.domains.account.domain.Account;
import com.pet.domains.account.dto.request.AccountAreaUpdateParam;
import com.pet.domains.account.dto.response.AccountAreaReadResults;
import com.pet.domains.area.domain.InterestArea;
import com.pet.domains.area.domain.Town;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InterestAreaMapper {

    @Mappings({
        @Mapping(target = "id", source = "interestArea.id"),
        @Mapping(target = "cityId", source = "interestArea.town.city.id"),
        @Mapping(target = "cityName", source = "interestArea.town.city.name"),
        @Mapping(target = "townId", source = "interestArea.town.id"),
        @Mapping(target = "townName", source = "interestArea.town.name"),
        @Mapping(target = "defaultArea", source = "interestArea.selected"),
    })
    AccountAreaReadResults.Area toAreaResult(InterestArea interestArea, boolean checked);

    @Mapping(target = "selected", source = "area.defaultArea")
    InterestArea toEntity(Account account, AccountAreaUpdateParam.Area area, Town town);

}
