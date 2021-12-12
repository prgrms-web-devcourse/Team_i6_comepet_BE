package com.pet.domains.animal.mapper;

import com.pet.domains.animal.domain.Animal;
import com.pet.domains.animal.dto.response.AnimalReadResults;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnimalMapper {

    @Mapping(source = "animalKinds", target = "kinds")
    AnimalReadResults.Animal toAnimalOfAnimalReadResults(Animal animalEntity);

    List<AnimalReadResults.Animal> toAnimalReadResult(List<Animal> animalEntities);

    default AnimalReadResults toAnimalReadResults(List<AnimalReadResults.Animal> animalReadResult) {
        return AnimalReadResults.of(animalReadResult);
    }
}
