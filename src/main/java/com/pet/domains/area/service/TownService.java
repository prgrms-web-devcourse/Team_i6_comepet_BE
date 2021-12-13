package com.pet.domains.area.service;

import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import com.pet.domains.area.dto.request.TownCreateParams;
import com.pet.domains.area.repository.TownRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TownService {

    private final CityService cityService;

    private final TownRepository townRepository;

    @Transactional
    public void createTowns(String cityCode, TownCreateParams townCreateParams) {
        City city = cityService.getCityByCode(cityCode);
        List<Town> towns = townCreateParams.getTowns().stream()
            .map(townCreateParam -> Town.builder()
                .city(city)
                .name(townCreateParam.getName())
                .code(townCreateParam.getCode())
                .build())
            .collect(Collectors.toList());
        townRepository.saveAll(towns);
    }

    public Town getOrCreateTownByName(String cityName, String townName) {
        City foundCity = cityService.getCityByName(cityName);
        return townRepository.findByNameAndCity(townName, foundCity)
            .orElseGet(() -> townRepository.findByNameContainingAndCity(townName, foundCity)
                .orElseGet(() -> createTownByName(townName, foundCity))
            );
    }

    @Transactional
    public Town createTownByName(String townName, City city) {
        return townRepository.save(
            Town.builder()
                .name(townName)
                .city(city)
                .build()
        );
    }
}
