package com.pet.domains.account.repository;

import com.pet.domains.account.domain.SignEmail;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignEmailRepository extends JpaRepository<SignEmail, Long> {

    Optional<SignEmail> findByEmailAndVerifyKey(String email, String verifyKey);

    Optional<SignEmail> findByEmailAndIsCheckedTrue(String email);

}
