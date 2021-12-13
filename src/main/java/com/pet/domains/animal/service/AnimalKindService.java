package com.pet.domains.animal.service;

import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.dto.request.AnimalKindCreateParams;
import com.pet.domains.animal.repository.AnimalKindRepository;
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

    private final AnimalService animalService;

    @Transactional
    public void createAnimalKinds(String animalCode, AnimalKindCreateParams animalKindCreateParams) {
        animalKindRepository.saveAll(
            animalKindCreateParams.getAnimalKinds().stream()
                .map(animalKindCreateParam -> AnimalKind.builder()
                    .name(animalKindCreateParam.getName())
                    .code(animalKindCreateParam.getCode())
                    .animal(animalService.getAnimalByCode(animalCode))
                    .build())
                .collect(Collectors.toList())
        );
    }

    public AnimalKind getOrCreateAnimalKind(Long animalId, String animalKindName) {
        return animalKindRepository.findByName(animalKindName)
            .orElseGet(() -> createAnimalKindByName(animalKindName, animalService.getAnimalById(animalId)));
    }

    public AnimalKind getOrCreateAnimalKindByEtcAnimal(String animalKindName) {
        return animalKindRepository.findByName(animalKindName)
            .orElseGet(() -> createAnimalKindByEtcAnimal(animalKindName));
    }

    @Transactional
    public AnimalKind createAnimalKindByEtcAnimal(String animalKindName) {
        return createAnimalKindByName(animalKindName, animalService.getAnimalByName(ETC_ANIMAL_NAME));
    }

    @Transactional
    public AnimalKind createAnimalKindByName(String animalKindName, Animal animalById) {
        return animalKindRepository.save(
            AnimalKind.builder()
                .animal(animalById)
                .name(animalKindName)
                .build()
        );
    }

}
