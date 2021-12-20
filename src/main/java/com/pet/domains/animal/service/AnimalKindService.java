package com.pet.domains.animal.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.dto.request.AnimalKindCreateParams;
import com.pet.domains.animal.repository.AnimalKindRepository;
import com.pet.domains.animal.repository.AnimalRepository;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AnimalKindService {

    private static final String ETC_ANIMAL_NAME = "기타";

    private final AnimalKindRepository animalKindRepository;

    private final AnimalRepository animalRepository;

    @CacheEvict(cacheNames = "animals", allEntries = true)
    @Transactional
    public void bulkCreateAnimalKind(String animalCode, AnimalKindCreateParams animalKindCreateParams) {
        animalKindRepository.saveAll(
            animalKindCreateParams.getAnimalKinds().stream()
                .map(animalKindCreateParam -> AnimalKind.builder()
                    .name(animalKindCreateParam.getName())
                    .code(animalKindCreateParam.getCode())
                    .animal(getAnimalByCode(animalCode))
                    .build())
                .collect(Collectors.toList())
        );
    }

    @CacheEvict(cacheNames = "animals", allEntries = true)
    @Transactional
    public AnimalKind getOrCreateAnimalKind(Long animalId, String animalKindName) {
        return animalKindRepository.findByName(animalKindName)
            .orElseGet(() -> animalKindRepository.save(
                AnimalKind.builder()
                    .animal(getAnimalById(animalId))
                    .name(animalKindName)
                    .build())
            );
    }

    @CacheEvict(cacheNames = "animals", allEntries = true)
    @Transactional
    public AnimalKind getOrCreateAnimalKindByEtcAnimal(String animalKindName) {
        return animalKindRepository.findByName(animalKindName)
            .orElseGet(() -> animalKindRepository.save(
                AnimalKind.builder()
                    .animal(getAnimalByName(ETC_ANIMAL_NAME))
                    .name(animalKindName)
                    .build()));
    }

    private Animal getAnimalByCode(String animalCode) {
        log.debug("animalCode: {}", animalCode);
        return animalRepository.findByCode(animalCode)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);
    }

    private Animal getAnimalByName(String animalName) {
        log.debug("animalName: {}", animalName);
        return animalRepository.findByName(animalName)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);
    }

    private Animal getAnimalById(Long animalId) {
        log.debug("animalId: {}", animalId);
        return animalRepository.findById(animalId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);
    }

}
