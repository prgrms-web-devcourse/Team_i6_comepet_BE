package com.pet.domains.account.service;

import com.pet.common.jwt.JwtAuthentication;
import com.pet.common.jwt.JwtAuthenticationToken;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public Account login(String email, String password) {
        Validate.notBlank(email, "email must be provided.");
        Validate.notBlank(password, "password must be provided.");

        Account account = accountRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Could not found user for " + email));
        Validate.isTrue(account.isMatchPassword(passwordEncoder, password), "Bad credential");
        return account;
    }

    public JwtAuthentication createAuthentication(String principal, String credentials) {
        return (JwtAuthentication)authenticationManager
            .authenticate(new JwtAuthenticationToken(principal, credentials)).getPrincipal();
    }

}
