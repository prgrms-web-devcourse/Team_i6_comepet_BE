package com.pet.domains.area.repository;

import com.pet.domains.area.domain.City;
import com.pet.domains.area.domain.Town;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TownRepository extends JpaRepository<Town, Long> {

    Optional<Town> findByNameAndCity(String name, City city);

    Optional<Town> findByNameContainingAndCity(String name, City city);

}
