package com.pet.domains.area.mapper;


import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.dto.request.CityCreateParams;
import com.pet.domains.area.dto.response.CityReadResults;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City toEntity(CityCreateParams.City cityCreateParam);

    CityReadResults.City.Town toTownDto(Town town);

    CityReadResults.City toCityDto(City city, List<CityReadResults.City.Town> collect);

}
