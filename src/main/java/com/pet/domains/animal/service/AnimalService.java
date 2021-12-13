package com.pet.domains.animal.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.dto.response.AnimalReadResults;
import com.pet.domains.animal.mapper.AnimalMapper;
import com.pet.domains.animal.repository.AnimalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    private final AnimalMapper animalMapper;

    public AnimalReadResults getAnimals() {
        List<AnimalReadResults.Animal> animalReadResult =
            animalMapper.toAnimalReadResult(animalRepository.findAllWithAnimalKinds());
        return animalMapper.toAnimalReadResults(animalReadResult);
    }

    public Animal getAnimalByCode(String animalCode) {
        log.debug("animalCode: {}", animalCode);
        return animalRepository.findByCode(animalCode)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);
    }

    public Animal getAnimalByName(String animalName) {
        log.debug("animalName: {}", animalName);
        return animalRepository.findByName(animalName)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);
    }

    public Animal getAnimalById(Long animalId) {
        log.debug("animalId: {}", animalId);
        return animalRepository.findById(animalId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);
    }

}
