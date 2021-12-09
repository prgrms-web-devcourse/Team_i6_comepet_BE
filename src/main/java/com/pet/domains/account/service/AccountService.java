package com.pet.domains.account.service;

import com.pet.common.exception.ExceptionMessage;
import com.pet.domains.account.domain.Account;
import com.pet.domains.account.repository.AccountRepository;
import java.text.MessageFormat;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    public Account login(String email, String password) {
        Account account = accountRepository.findByEmail(email)
            .orElseThrow(ExceptionMessage.NOT_FOUND_ACCOUNT::getException);
        return checkPassword(password, account);
    }

    public Account checkLoginAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(
            () -> new UsernameNotFoundException(MessageFormat.format("유저를 찾지 못 했습니다.{0}", accountId))
        );
    }

    @Transactional
    public void signUp(String email, String password) {
        accountRepository.save(new Account(email, passwordEncoder.encode(password)));
    }

    private Account checkPassword(String password, Account account) {
        if (account.isMatchPassword(passwordEncoder, password)) {
            return account;
        }
        throw ExceptionMessage.INVALID_LOGIN.getException();
    }

}
