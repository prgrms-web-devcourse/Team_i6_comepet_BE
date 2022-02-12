package com.pet.domains.auth.repository;

import com.pet.domains.auth.oauth2.RefreshToken;
import java.util.Optional;
import java.util.stream.DoubleStream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String refreshToken);
}
