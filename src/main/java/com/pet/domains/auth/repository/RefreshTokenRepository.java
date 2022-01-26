package com.pet.domains.auth.repository;

import com.pet.domains.auth.oauth2.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

}
