package com.pet.domains.area.repository;

import com.pet.domains.area.domain.InterestArea;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterestAreaRepository extends JpaRepository<InterestArea, Long> {

    @Query("select i from InterestArea i "
        + "join fetch i.town t "
        + "left join fetch t.city c "
        + "where i.account.id = :accountId")
    List<InterestArea> findByAccountId(@Param("accountId") Long accountId);

    void deleteAllByAccountId(Long id);

}
