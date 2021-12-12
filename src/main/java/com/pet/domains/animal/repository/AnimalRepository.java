package com.pet.domains.animal.repository;

import com.pet.domains.animal.domain.Animal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByCode(String code);

    Optional<Animal> findByName(String name);

}
