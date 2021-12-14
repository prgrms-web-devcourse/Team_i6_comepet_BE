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

    @Transactional
    public AnimalKind getOrCreateAnimalKind(Long animalId, String animalKindName) {
        return animalKindRepository.findByName(animalKindName)
            .orElseGet(() -> createAnimalKindByName(animalKindName, getAnimalById(animalId)));
    }

    @Transactional
    public AnimalKind getOrCreateAnimalKindByEtcAnimal(String animalKindName) {
        return animalKindRepository.findByName(animalKindName)
            .orElseGet(() -> createAnimalKindByEtcAnimal(animalKindName));
    }

    private AnimalKind createAnimalKindByEtcAnimal(String animalKindName) {
        return createAnimalKindByName(animalKindName, getAnimalByName(ETC_ANIMAL_NAME));
    }

    private AnimalKind createAnimalKindByName(String animalKindName, Animal animal) {
        return animalKindRepository.save(
            AnimalKind.builder()
                .animal(animal)
                .name(animalKindName)
                .build()
        );
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
