package com.pet.domains.animal.repository;

import com.pet.domains.animal.domain.Animal;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Override
    @EntityGraph(attributePaths = {"animalKinds"}, type = EntityGraphType.LOAD)
    List<Animal> findAll();

    Optional<Animal> findByCode(String code);

    Optional<Animal> findByName(String name);

}
