package com.pet.domains.account.repository;

import com.pet.domains.account.domain.Account;
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

}
