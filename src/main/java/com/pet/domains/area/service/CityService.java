package com.pet.domains.area.service;

import com.pet.domains.area.domain.City;
import com.pet.domains.area.dto.request.CityCreateParams;
import com.pet.domains.area.mapper.CityMapper;
import com.pet.domains.area.repository.CityRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
            .map(cityMapper::toCity)
            .collect(Collectors.toList());
        cityRepository.saveAll(cities);
    }

}
