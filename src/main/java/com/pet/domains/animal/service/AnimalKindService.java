package com.pet.domains.animal.service;


import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.domain.AnimalKind;
import com.pet.domains.animal.dto.request.AnimalKindCreateParams;
import com.pet.domains.animal.repository.AnimalKindRepository;
import com.pet.domains.animal.repository.AnimalRepository;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnimalKindService {

    private static final int TRANSACTION_CHUNK_LIMIT = 100;

    private final AnimalKindRepository animalKindRepository;

    private final AnimalRepository animalRepository;

    public void createAnimalKinds(String animalCode, AnimalKindCreateParams animalKindCreateParams) {

        Animal foundAnimal = animalRepository.findByCode(animalCode)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);

        Set<AnimalKind> transactionChunk = new HashSet<>();
        animalKindCreateParams.getAnimalKinds().forEach(animalKindCreateParam -> {
                transactionChunk.add(AnimalKind.builder()
                    .name(animalKindCreateParam.getName())
                    .code(animalKindCreateParam.getCode())
                    .animal(foundAnimal)
                    .build()
                );
                if (transactionChunk.size() == TRANSACTION_CHUNK_LIMIT) {
                    animalKindRepository.saveAll(transactionChunk);
                    transactionChunk.clear();
                    log.info("AnimalKind set has saved with chunk unit");
                }
            }
        );
        animalKindRepository.saveAll(transactionChunk);
    }

    @Transactional
    public AnimalKind getOrCreateByAnimalKind(Long animalId, String animalKindName) {

        Animal foundAnimal = animalRepository.findById(animalId)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ANIMAL::getException);

        return animalKindRepository.findByName(animalKindName)
            .orElseGet(() -> animalKindRepository.save(
                AnimalKind.builder()
                    .animal(foundAnimal)
                    .name(animalKindName)
                    .build()
            ));
    }

}
