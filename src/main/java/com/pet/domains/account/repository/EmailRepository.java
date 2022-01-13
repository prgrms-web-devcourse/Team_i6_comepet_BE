package com.pet.domains.account.repository;

import com.pet.domains.account.domain.Email;
import org.springframework.data.repository.CrudRepository;

public interface EmailRepository extends CrudRepository<Email, String> {

}
