package com.pet.domains.area.repository;

import com.pet.domains.area.domain.City;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByCode(String code);

    Optional<City> findByName(String name);

    @EntityGraph(attributePaths = "towns", type = EntityGraph.EntityGraphType.LOAD)
    List<City> findAll();
}
