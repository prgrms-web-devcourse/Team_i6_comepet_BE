package com.pet.domains.area.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.dto.request.TownCreateParams;
import com.pet.domains.area.repository.CityRepository;
import com.pet.domains.area.repository.TownRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TownService {

    private final CityRepository cityRepository;

    private final TownRepository townRepository;

    public void createTowns(String cityCode, TownCreateParams townCreateParams) {
        City city = getCityByCode(cityCode);
        List<Town> towns = townCreateParams.getTowns().stream()
            .map(townCreateParam -> Town.builder()
                .city(city)
                .name(townCreateParam.getName())
                .code(townCreateParam.getCode())
                .build())
            .collect(Collectors.toList());
        townRepository.saveAll(towns);
    }

    private City getCityByCode(String cityCode) {
        return cityRepository.findByCode(cityCode)
            .orElseThrow(ExceptionMessage.NOT_FOUND_CITY::getException);
    }

}
