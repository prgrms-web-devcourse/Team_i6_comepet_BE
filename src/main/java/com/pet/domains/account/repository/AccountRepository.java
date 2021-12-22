package com.pet.domains.account.repository;

import com.pet.domains.account.domain.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query("select a "
        + "from Account a"
        + "    join fetch a.group g left join fetch g.permissions gp join fetch gp.permission "
        + "where a.email = :email")
    Optional<Account> findByEmail(String email);

    boolean existsByEmail(String email);

    @Query("select a from Account a join fetch a.image where a.id = :id")
    Optional<Account> findByIdAndImage(Long id);

    @Query("select a from Account a inner join InterestArea ia"
        + " on a.id = ia.account.id and ia.selected = true and ia.town.id = :townId"
        + " where a.notification = true")
    List<Account> findAllByNotificationSubscribers(Long townId);

}
