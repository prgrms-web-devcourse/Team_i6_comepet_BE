package com.pet.domains.area.mapper;


import com.pet.domains.area.domain.City;
import com.pet.domains.area.dto.request.CityCreateParams;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {

    City toEntity(CityCreateParams.City cityCreateParam);

}
