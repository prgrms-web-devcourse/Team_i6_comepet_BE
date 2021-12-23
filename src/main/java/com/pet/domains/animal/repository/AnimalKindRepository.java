package com.pet.domains.animal.repository;

import com.pet.domains.animal.domain.AnimalKind;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalKindRepository extends JpaRepository<AnimalKind, Long> {

    Optional<AnimalKind> findByName(String name);

    Optional<AnimalKind> findByNameAndAnimalId(String name, Long animalId);

}
