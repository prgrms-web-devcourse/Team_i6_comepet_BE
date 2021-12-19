package com.pet.domains.area.service;

import com.pet.domains.area.domain.City;
import com.pet.domains.area.dto.request.CityCreateParams;
import com.pet.domains.area.dto.response.CityReadResults;
import com.pet.domains.area.mapper.CityMapper;
import com.pet.domains.area.repository.CityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CityService {

    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    @Transactional
    public void createCites(CityCreateParams cityCreateParams) {
        List<City> cities = cityCreateParams.getCities().stream()
            .map(cityMapper::toEntity)
            .collect(Collectors.toList());
        cityRepository.saveAll(cities);
    }

    @Cacheable(cacheNames = "cities", unless = "#result == null || #result.empty")
    public CityReadResults getAllTownAndCity() {
        List<City> result = cityRepository.findAll();
        return CityReadResults.of(result.stream()
            .map(city -> cityMapper.toCityDto(city, city.getTowns().stream()
                .map(cityMapper::toTownDto)
                .collect(Collectors.toList())))
            .collect(Collectors.toList()));
    }
}
