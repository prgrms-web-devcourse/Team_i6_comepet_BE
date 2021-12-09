package com.pet.domains.auth.service;

import com.pet.common.jwt.JwtAuthentication;
import com.pet.common.jwt.JwtAuthenticationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public JwtAuthentication authenticate(String principal, String credentials) {
        return (JwtAuthentication)authenticationManager
            .authenticate(new JwtAuthenticationToken(principal, credentials)).getPrincipal();
    }
}
