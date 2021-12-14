package com.pet.domains.area.repository;

import com.pet.domains.area.domain.InterestArea;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InterestAreaRepository extends JpaRepository<InterestArea, Long> {

    @EntityGraph(attributePaths = "town")
    @Query("select i from InterestArea i join fetch i.town t left join fetch t.city c")
    List<InterestArea> findByAccountId(Long id);
}
