package com.pet.domains.animal.service;

import com.pet.domains.animal.dto.response.AnimalReadResults;
import com.pet.domains.animal.mapper.AnimalMapper;
import com.pet.domains.animal.repository.AnimalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    private final AnimalMapper animalMapper;

    @Cacheable(
        cacheNames = "animals",
        key = "animals",
        unless = "#result == null || #result.getAnimals().isEmpty()"
    )
    public AnimalReadResults getAnimals() {
        List<AnimalReadResults.Animal> result =
            animalMapper.toAnimalReadResult(animalRepository.findAllWithAnimalKinds());
        return animalMapper.toAnimalReadResults(result);
    }

}
