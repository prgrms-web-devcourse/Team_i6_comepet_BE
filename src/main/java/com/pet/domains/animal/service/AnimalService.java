package com.pet.domains.animal.service;

import com.pet.domains.animal.dto.response.AnimalReadResults;
import com.pet.domains.animal.mapper.AnimalMapper;
import com.pet.domains.animal.repository.AnimalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class AnimalService {

    private final AnimalRepository animalRepository;

    private final AnimalMapper animalMapper;

    public AnimalReadResults getAnimals() {
        List<AnimalReadResults.Animal> animalReadResult =
            animalMapper.toAnimalReadResult(animalRepository.findAll());
        return animalMapper.toAnimalReadResults(animalReadResult);
    }

}
