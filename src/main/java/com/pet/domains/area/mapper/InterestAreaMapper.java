package com.pet.domains.area.mapper;

import com.pet.domains.account.dto.response.AccountAreaReadResults;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface InterestAreaMapper {

    @Mappings({
        @Mapping(target = "cityId", source = "city.id"),
        @Mapping(target = "cityName", source = "city.name"),
        @Mapping(target = "townId", source = "town.id"),
        @Mapping(target = "townName", source = "town.name"),
        @Mapping(target = "defaultArea", source = "town.name"),
    })
    AccountAreaReadResults.Area toAreaResult(City city, Town town, boolean selected);

}
