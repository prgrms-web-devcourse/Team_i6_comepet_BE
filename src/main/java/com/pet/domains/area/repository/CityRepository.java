package com.pet.domains.area.repository;

import com.pet.domains.area.domain.City;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByCode(String code);
}
