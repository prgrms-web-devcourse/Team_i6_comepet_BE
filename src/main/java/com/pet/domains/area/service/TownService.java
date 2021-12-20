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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TownService {

    private final CityRepository cityRepository;

    private final TownRepository townRepository;

    @CacheEvict(cacheNames = "cities", allEntries = true)
    @Transactional
    public void bulkCreateTowns(String cityCode, TownCreateParams townCreateParams) {
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

    @CacheEvict(cacheNames = "cities", allEntries = true)
    @Transactional
    public Town getOrCreateTownByName(String cityName, String townName) {
        City foundCity = getCityByName(cityName);
        return townRepository.findByNameAndCity(townName, foundCity)
            .orElseGet(() -> townRepository.findByNameContainingAndCity(townName, foundCity)
                .orElseGet(() -> townRepository.save(
                    Town.builder()
                        .name(townName)
                        .city(foundCity)
                        .build()
                ))
            );
    }

    private City getCityByCode(String cityCode) {
        return cityRepository.findByCode(cityCode)
            .orElseThrow(ExceptionMessage.NOT_FOUND_CITY::getException);
    }

    private City getCityByName(String cityName) {
        return cityRepository.findByName(cityName)
            .orElseThrow(ExceptionMessage.NOT_FOUND_CITY::getException);
    }
}
